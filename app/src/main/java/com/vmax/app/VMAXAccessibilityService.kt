package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.workflow.ExecutionTracker
import com.vmax.workflow.ActionOrchestrator
import com.vmax.workflow.WorkflowController
import com.vmax.workflow.UIElement
import com.vmax.workflow.BoundingBox
import com.vmax.workflow.WorkflowAction
import com.vmax.common.Result
import com.vmax.runtime.MetricsCollector
import com.vmax.runtime.ExecutionRecorder
import com.vmax.app.AndroidLogger
import com.vmax.app.AndroidExecutionHistoryStore
import com.vmax.app.AndroidExecutionRecorder
import com.vmax.app.AndroidMetricsCollector
import com.vmax.core_intelligence.ScreenAnalyzer
import com.vmax.core_intelligence.UIEvidenceCollector
import com.vmax.core_intelligence.TextClassifier
import com.vmax.core_intelligence.OcrResult

class VMAXAccessibilityService : AccessibilityService() {
    companion object {
        private const val TAG = "VMAX_EXECUTION_SERVICE"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"
    }

    private var isServiceReady = false
    private var currentSessionId = ""

    private lateinit var orchestrator: ActionOrchestrator
    private lateinit var tracker: ExecutionTracker
    private lateinit var analyzer: ScreenAnalyzer
    private lateinit var classifier: TextClassifier
    private lateinit var evidenceCollector: UIEvidenceCollector
    private lateinit var historyStore: AndroidExecutionHistoryStore
    private lateinit var recorder: ExecutionRecorder
    private lateinit var metrics: MetricsCollector
    private lateinit var executor: ActionExecutor
    private lateinit var workflowController: WorkflowController

    // Store passenger details for state updates
    private var passengerName = ""
    private var passengerAge = ""

    // --- Public Contract ---
    fun startWorkflow(
        from: String, to: String, date: String, train: String,
        trainClass: String, name: String, age: String, gender: String, meal: String
    ) {
        if (!isServiceReady) {
            Log.e(TAG, "Service not ready")
            return
        }

        passengerName = name
        passengerAge = age

        currentSessionId = "SESSION_${System.currentTimeMillis()}"
        tracker.startSession(currentSessionId)
        metrics.startMetrics(currentSessionId)
        recorder.recordEvent(ExecutionEvent.SessionStarted(currentSessionId))

        val details = WorkflowController.PassengerDetails(
            from, to, date, train, trainClass, name, age, gender, meal
        )

        if (workflowController.startWorkflow(details, currentSessionId)) {
            Log.i(TAG, "Workflow armed")
        } else {
            Log.e(TAG, "Failed to start workflow")
            stopWorkflow()
        }
    }

    fun stopWorkflow() {
        if (workflowController.getCurrentState() in listOf(
            WorkflowController.WorkflowState.STOPPED,
            WorkflowController.WorkflowState.IDLE
        )) return

        val sessionId = workflowController.getSessionId()
        if (sessionId.isNotEmpty()) {
            tracker.stopSession(sessionId)
            recorder.recordEvent(ExecutionEvent.SessionStopped(sessionId))
            metrics.stopMetrics(sessionId, "STOPPED")
        }
        workflowController.stopWorkflow()
        currentSessionId = ""
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val logger = AndroidLogger()
        
        executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(logger)
        orchestrator = ActionOrchestrator(executor, tracker)
        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()
        evidenceCollector = UIEvidenceCollector(logger)
        classifier = TextClassifier()
        analyzer = ScreenAnalyzer(evidenceCollector, logger)
        
        workflowController = WorkflowController(
            orchestrator, analyzer, classifier, metrics, recorder
        )
        
        isServiceReady = true
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || !isServiceReady) return
        if (event.packageName?.toString() != IRCTC_PACKAGE) return
        
        val root = rootInActiveWindow ?: return
        val uiElements = extractUiElements(root)

        try {
            evidenceCollector.updateUiElements(uiElements)

            // Convert to workflow UIElement type
            val workflowElements = uiElements.map { convertToWorkflowUIElement(it) }

            // Handle state-specific actions first
            val currentState = workflowController.getCurrentState()
            val stateAction = workflowController.handleStateAction(
                currentState,
                workflowElements
            )

            if (stateAction != null) {
                executeWorkflowAction(stateAction)
                return
            }

            // Handle screen analysis
            val analysis = analyzer.analyzeCurrentScreen()
            val ocrText = analysis.evidence?.ocrEvidence?.fullText ?: ""
            val ocrBlocks = analysis.evidence?.ocrEvidence?.rawBlocks ?: emptyList()

            val action = workflowController.handleScreenAnalysis(
                analysis,
                ocrText,
                ocrBlocks
            )

            if (action != null) {
                executeWorkflowAction(action)
            }

        } finally {
            root.recycle()
        }
    }

    // --- UI Extraction ---
    private fun extractUiElements(root: AccessibilityNodeInfo): List<UIEvidenceCollector.ScreenEvidence.UIElement> {
        val elements = mutableListOf<UIEvidenceCollector.ScreenEvidence.UIElement>()
        val queue = ArrayDeque<AccessibilityNodeInfo>().apply { add(root) }
        
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val bounds = android.graphics.Rect().also { node.getBoundsInScreen(it) }
            
            elements.add(
                UIEvidenceCollector.ScreenEvidence.UIElement(
                    id = node.viewIdResourceName ?: "",
                    type = node.className?.toString() ?: "",
                    text = node.text?.toString() ?: "",
                    contentDescription = node.contentDescription?.toString(),
                    bounds = OcrResult.BoundingBox(
                        bounds.left, bounds.top, bounds.right, bounds.bottom
                    ),
                    isClickable = node.isClickable,
                    isEditable = node.isEditable,
                    hint = node.hintText?.toString()
                )
            )
            
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
            if (node !== root) node.recycle()
        }
        return elements
    }

    private fun convertToWorkflowUIElement(
        element: UIEvidenceCollector.ScreenEvidence.UIElement
    ): UIElement {
        return UIElement(
            id = element.id,
            type = element.type,
            text = element.text,
            contentDescription = element.contentDescription,
            bounds = element.bounds?.let { 
                BoundingBox(it.left, it.top, it.right, it.bottom)
            },
            isClickable = element.isClickable,
            isEditable = element.isEditable,
            hint = element.hint
        )
    }

    // --- Action Execution ---
    private fun executeWorkflowAction(action: WorkflowAction) {
        val sessionId = workflowController.getSessionId()
        
        when (action) {
            is WorkflowAction.Click -> {
                val result = if (action.targetId != null && action.targetId!!.isNotEmpty()) {
                    orchestrator.click(
                        targetId = action.targetId,
                        sessionId = sessionId
                    )
                } else if (action.coordinates != null) {
                    orchestrator.click(
                        coordinates = action.coordinates,
                        sessionId = sessionId
                    )
                } else {
                    Log.e(TAG, "Click action has no target ID or coordinates")
                    return
                }
                
                if (result is Result.Success) {
                    Log.d(TAG, "Click action succeeded")
                } else {
                    Log.e(TAG, "Click action failed: ${(result as Result.Error).error.message}")
                }
            }
            
            is WorkflowAction.SetText -> {
                if (action.targetId != null && action.targetId!!.isNotEmpty()) {
                    val result = orchestrator.setText(
                        targetId = action.targetId,
                        text = action.text,
                        sessionId = sessionId
                    )
                    
                    if (result is Result.Success) {
                        Log.d(TAG, "SetText action succeeded")
                        // Update state after successful text input
                        when (action.text) {
                            passengerName -> {
                                workflowController.updateState(
                                    WorkflowController.WorkflowState.PASSENGER_NAME_TYPED
                                )
                            }
                            passengerAge -> {
                                workflowController.updateState(
                                    WorkflowController.WorkflowState.PASSENGER_AGE_TYPED
                                )
                            }
                        }
                    } else {
                        Log.e(TAG, "SetText action failed: ${(result as Result.Error).error.message}")
                    }
                } else {
                    Log.e(TAG, "SetText action has no target ID")
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted")
        stopWorkflow()
    }
}
