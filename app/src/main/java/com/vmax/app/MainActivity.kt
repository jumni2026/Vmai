package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vmax.model.*
import com.vmax.workflow.WorkflowState
import com.vmax.runtime.AndroidExecutionHistoryStore
import com.vmax.runtime.AndroidMetricsCollector
import com.vmax.runtime.AndroidExecutionHistoryRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create runtime objects once for this Activity.
        val historyStore = AndroidExecutionHistoryStore(this)
        val metricsCollector = AndroidMetricsCollector()
        val historyRepository =
            AndroidExecutionHistoryRepository(
                historyStore,
                metricsCollector
            )

        setContent {
            VMAXTheme {

                var showHistory by remember {
                    mutableStateOf(false)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showHistory) {
                        HistoryScreen(
                            repository = historyRepository,
                            onBack = {
                                showHistory = false
                            }
                        )
                    } else {
                        VMAXDashboard(
                            onHistoryClick = {
                                showHistory = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VMAXDashboard(
    onHistoryClick: () -> Unit
) {
    val viewModel: MainViewModel = viewModel()

    val trainNumber by viewModel.trainNumber.collectAsState()
    val trainName by viewModel.trainName.collectAsState()
    val classType by viewModel.classType.collectAsState()
    val quota by viewModel.quota.collectAsState()

    var fromStationInput by remember {
        mutableStateOf("")
    }

    var toStationInput by remember {
        mutableStateOf("")
    }

    val journeyDate by viewModel.journeyDate.collectAsState()

    val passengerName by viewModel.passengerName.collectAsState()
    val passengerAge by viewModel.passengerAge.collectAsState()
    val passengerGender by viewModel.passengerGender.collectAsState()
    val passengerMobile by viewModel.passengerMobile.collectAsState()

    val validationError by viewModel.validationError.collectAsState()
    val workflowState by viewModel.workflowState.collectAsState()

    var showPassengerDialog by remember {
        mutableStateOf(false)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var quotaExpanded by remember {
        mutableStateOf(false)
    }

    var classExpanded by remember {
        mutableStateOf(false)
    }

    var mealExpanded by remember {
        mutableStateOf(false)
    }

    // Kept local because MainViewModel currently has no passengerMeal state.
    var passengerMeal by remember {
        mutableStateOf("NO PREFERENCE")
    }

    val classOptions =
        listOf(
            "1A",
            "2A",
            "3A",
            "SL",
            "CC",
            "EC",
            "3E",
            "2S",
            "FC"
        )

    val mealOptions =
        listOf(
            "VEG",
            "NON-VEG",
            "NO PREFERENCE"
        )

    val currentWorkflowState = workflowState

    val statusText = when (currentWorkflowState) {

        WorkflowState.CONFIGURED ->
            "CONFIGURED (Waiting for Engine)"

        WorkflowState.RUNNING ->
            "RUNNING (Target: ${
                trainNumber.takeIf {
                    it.isNotBlank()
                } ?: "N/A"
            })"

        WorkflowState.ERROR ->
            "ERROR"

        else ->
            "IDLE"
    }

    val workflowActive =
        currentWorkflowState == WorkflowState.RUNNING ||
        currentWorkflowState == WorkflowState.CONFIGURED

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                title = {
                    Text("VMAX Enterprise")
                },
                actions = {
                    TextButton(
                        onClick = onHistoryClick
                    ) {
                        Text("History")
                    }
                }
            )
        },

        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp
            ) {
                Button(
                    onClick = {
                        viewModel.updateFromStation(
                            fromStationInput
                        )

                        viewModel.updateToStation(
                            toStationInput
                        )

                        if (viewModel.isWorkflowActive()) {
                            viewModel.stopWorkflow()
                        } else {
                            viewModel.startWorkflow()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                        .height(50.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (workflowActive) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                    )
                ) {
                    Text(
                        text =
                            if (workflowActive) {
                                "STOP WORKFLOW ENGINE"
                            } else {
                                "CONFIGURE & START ENGINE"
                            },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .imePadding()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "VMAX ENTERPRISE",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "VERSION: 2.6.1 FINAL 🔒",
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "🎯 Target Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value = trainNumber,
                        onValueChange = {
                            viewModel.updateTrainNumber(it)
                        },
                        label = {
                            Text("Train Number")
                        },
                        placeholder = {
                            Text("e.g., 20503")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError =
                            validationError
                                ?.contains("Train Number") == true,
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Number
                            ),
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    OutlinedTextField(
                        value = trainName,
                        onValueChange = {},
                        label = {
                            Text("Train Name")
                        },
                        placeholder = {
                            Text("Auto-populated")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        isError =
                            validationError
                                ?.contains("Train Name") == true,
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = classExpanded,
                        onExpandedChange = {
                            classExpanded = it
                        }
                    ) {

                        OutlinedTextField(
                            value = classType,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Class Type")
                            },
                            placeholder = {
                                Text("e.g., 3A")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = classExpanded
                                )
                            },
                            isError =
                                validationError
                                    ?.contains("Class Type") == true,
                            singleLine = true
                        )

                        ExposedDropdownMenu(
                            expanded = classExpanded,
                            onDismissRequest = {
                                classExpanded = false
                            }
                        ) {

                            classOptions.forEach { option ->

                                DropdownMenuItem(
                                    text = {
                                        Text(option)
                                    },
                                    onClick = {
                                        viewModel
                                            .updateClassType(
                                                option
                                            )

                                        classExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Quota",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = {
                                quotaExpanded = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text =
                                    quota?.name
                                        ?: "Select Quota"
                            )
                        }

                        DropdownMenu(
                            expanded = quotaExpanded,
                            onDismissRequest = {
                                quotaExpanded = false
                            }
                        ) {

                            Quota.values().forEach { q ->

                                DropdownMenuItem(
                                    text = {
                                        Text(q.name)
                                    },
                                    onClick = {
                                        viewModel
                                            .updateQuota(q)

                                        quotaExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedTextField(
                            value = fromStationInput,
                            onValueChange = {
                                fromStationInput = it
                            },
                            label = {
                                Text("From Station Code")
                            },
                            placeholder = {
                                Text("e.g., NDLS")
                            },
                            modifier = Modifier.weight(1f),
                            isError =
                                validationError
                                    ?.contains("From Station") == true,
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        OutlinedTextField(
                            value = toStationInput,
                            onValueChange = {
                                toStationInput = it
                            },
                            label = {
                                Text("To Station Code")
                            },
                            placeholder = {
                                Text("e.g., MUMBAI")
                            },
                            modifier = Modifier.weight(1f),
                            isError =
                                validationError
                                    ?.contains("To Station") == true,
                            singleLine = true
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    // Calendar-based date selection.
                    OutlinedTextField(
                        value = journeyDate,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Journey Date")
                        },
                        placeholder = {
                            Text("Select Journey Date")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError =
                            validationError
                                ?.contains("Date") == true,
                        singleLine = true,
                        trailingIcon = {
                            TextButton(
                                onClick = {
                                    showDatePicker = true
                                }
                            ) {
                                Text("📅")
                            }
                        }
                    )

                    if (validationError != null) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = validationError!!,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "👤 Passenger Data Notebook",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            if (passengerName.isNotBlank()) {
                                "Status: Configured ($passengerName, $passengerAge Yrs)"
                            } else {
                                "Status: Pop-up Interface Ready"
                            }
                    )

                    Button(
                        onClick = {
                            showPassengerDialog = true
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Configure via Pop-up")
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text =
                    "Automation Status: $statusText",
                fontWeight = FontWeight.Bold,
                color =
                    when (currentWorkflowState) {

                        WorkflowState.RUNNING ->
                            MaterialTheme
                                .colorScheme
                                .primary

                        WorkflowState.ERROR ->
                            MaterialTheme
                                .colorScheme
                                .error

                        else ->
                            MaterialTheme
                                .colorScheme
                                .onBackground
                    }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }

    // ---------------------------------------------------------
    // DATE PICKER
    // ---------------------------------------------------------

    if (showDatePicker) {

        val datePickerState =
            rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        datePickerState
                            .selectedDateMillis
                            ?.let { millis ->

                                val formatter =
                                    SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.US
                                    )

                                val selectedDate =
                                    formatter.format(
                                        Date(millis)
                                    )

                                viewModel
                                    .updateJourneyDate(
                                        selectedDate
                                    )
                            }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }

    // ---------------------------------------------------------
    // PASSENGER DIALOG
    // ---------------------------------------------------------

    if (showPassengerDialog) {

        AlertDialog(
            onDismissRequest = {
                showPassengerDialog = false
            },

            title = {
                Text("Add Passenger Data")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = passengerName,
                        onValueChange = {
                            viewModel
                                .updatePassengerName(it)
                        },
                        label = {
                            Text("Passenger Name")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value = passengerAge,
                        onValueChange = {
                            viewModel
                                .updatePassengerAge(it)
                        },
                        label = {
                            Text("Age")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Number
                            )
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Gender",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    var genderExpanded by remember {
                        mutableStateOf(false)
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = {
                                genderExpanded = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                passengerGender.ifBlank {
                                    "Select Gender"
                                }
                            )
                        }

                        DropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = {
                                genderExpanded = false
                            }
                        ) {

                            listOf(
                                "MALE",
                                "FEMALE",
                                "OTHER"
                            ).forEach { gender ->

                                DropdownMenuItem(
                                    text = {
                                        Text(gender)
                                    },
                                    onClick = {

                                        viewModel
                                            .updatePassengerGender(
                                                gender
                                            )

                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value = passengerMobile,
                        onValueChange = {
                            viewModel
                                .updatePassengerMobile(it)
                        },
                        label = {
                            Text("Mobile (10 digits)")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Phone
                            )
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Meal Preference",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = {
                                mealExpanded = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(passengerMeal)
                        }

                        DropdownMenu(
                            expanded = mealExpanded,
                            onDismissRequest = {
                                mealExpanded = false
                            }
                        ) {

                            mealOptions.forEach { meal ->

                                DropdownMenuItem(
                                    text = {
                                        Text(meal)
                                    },
                                    onClick = {
                                        passengerMeal = meal
                                        mealExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },

            confirmButton = {

                Button(
                    onClick = {
                        showPassengerDialog = false
                    }
                ) {
                    Text("Save")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showPassengerDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
