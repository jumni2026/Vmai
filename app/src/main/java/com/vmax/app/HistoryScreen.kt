package com.vmax.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vmax.runtime.ExecutionEvent
import com.vmax.runtime.ExecutionHistoryRepository
import com.vmax.runtime.MetricsCollector
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    repository: AndroidExecutionHistoryRepository,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var sessionHistories by remember { mutableStateOf<List<ExecutionHistoryRepository.SessionHistory>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var expandedSessionId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        sessionHistories = repository.getAllSessionHistories()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Execution History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (sessionHistories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No execution history found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                items(sessionHistories) { history ->
                    SessionCard(
                        history = history,
                        expanded = expandedSessionId == history.sessionId,
                        onExpand = { expandedSessionId = if (expandedSessionId == history.sessionId) null else history.sessionId }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun SessionCard(
    history: ExecutionHistoryRepository.SessionHistory,
    expanded: Boolean,
    onExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpand() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Summary header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Session: ${history.sessionId.takeLast(8)}",
                    style = MaterialTheme.typography.titleMedium
                )
                val status = history.metrics?.status ?: "UNKNOWN"
                Text(
                    text = status,
                    color = when (status) {
                        "COMPLETED" -> MaterialTheme.colorScheme.primary
                        "STOPPED" -> MaterialTheme.colorScheme.onSurface
                        "USER_BOUNDARY" -> MaterialTheme.colorScheme.error
                        "RUNNING" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("Total", style = MaterialTheme.typography.labelMedium)
                    Text("${history.metrics?.totalActions ?: 0}", style = MaterialTheme.typography.bodyLarge)
                }
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("✅ Success", style = MaterialTheme.typography.labelMedium)
                    Text("${history.metrics?.successActions ?: 0}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                }
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("❌ Failed", style = MaterialTheme.typography.labelMedium)
                    Text("${history.metrics?.failedActions ?: 0}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                // Show detailed events
                Text("Event Timeline", style = MaterialTheme.typography.titleSmall)
                history.events.forEach { event ->
                    EventRow(event = event)
                }
                // Show error distribution if any
                val errorDist = history.metrics?.errorDistribution
                if (!errorDist.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Error Distribution", style = MaterialTheme.typography.titleSmall)
                    errorDist.forEach { (code, count) ->
                        Row {
                            Text("- $code: $count", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventRow(event: ExecutionEvent) {
    val summary = when (event) {
        is ExecutionEvent.SessionStarted -> "Session Started"
        is ExecutionEvent.SessionStopped -> "Session Stopped"
        is ExecutionEvent.SessionError -> "Session Error: ${event.errorCode}"
        is ExecutionEvent.WorkflowStateChanged -> "State: ${event.fromState} → ${event.toState}"
        is ExecutionEvent.ActionDispatched -> "Dispatch: ${event.actionType} (target: ${event.targetId ?: event.targetText ?: "none"})"
        is ExecutionEvent.ActionSucceeded -> "Success: ${event.actionType} - ${event.resultMessage ?: ""}"
        is ExecutionEvent.ActionFailed -> "Failed: ${event.actionType} - ${event.errorCode}: ${event.errorMessage}"
    }
    Text(
        text = summary,
        fontSize = 14.sp,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
