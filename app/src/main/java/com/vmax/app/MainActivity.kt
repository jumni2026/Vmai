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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VMAXTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VMAXDashboard()
                }
            }
        }
    }
}

@Composable
fun VMAXDashboard() {

    val viewModel: MainViewModel = viewModel()

    val trainNumber by viewModel.trainNumber.collectAsState()
    val trainName by viewModel.trainName.collectAsState()
    val classType by viewModel.classType.collectAsState()
    val quota by viewModel.quota.collectAsState()
    val fromStation by viewModel.fromStation.collectAsState()
    val toStation by viewModel.toStation.collectAsState()
    val journeyDate by viewModel.journeyDate.collectAsState()

    val passengerName by viewModel.passengerName.collectAsState()
    val passengerAge by viewModel.passengerAge.collectAsState()
    val passengerGender by viewModel.passengerGender.collectAsState()
    val passengerMobile by viewModel.passengerMobile.collectAsState()

    val validationError by viewModel.validationError.collectAsState()
    val workflowState by viewModel.workflowState.collectAsState()

    var showPassengerDialog by remember { mutableStateOf(false) }
    var quotaExpanded by remember { mutableStateOf(false) }

    val currentWorkflowState = workflowState

    val statusText = when (currentWorkflowState) {
        is WorkflowState.CONFIGURED ->
            "CONFIGURED (Waiting for Engine)"

        is WorkflowState.RUNNING ->
            "RUNNING (Target: ${trainNumber.takeIf { it.isNotBlank() } ?: "N/A"})"

        is WorkflowState.ERROR ->
            "ERROR: ${currentWorkflowState.reason}"

        else ->
            "IDLE"
    }

    val workflowActive =
        currentWorkflowState is WorkflowState.RUNNING ||
        currentWorkflowState is WorkflowState.CONFIGURED

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp
            ) {
                Button(
                    onClick = {
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
                        containerColor = if (workflowActive) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                ) {
                    Text(
                        text = if (workflowActive) {
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
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------------------------------------------------------
            // HEADER
            // ---------------------------------------------------------

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

            Spacer(modifier = Modifier.height(24.dp))

            // ---------------------------------------------------------
            // TARGET SETTINGS
            // ---------------------------------------------------------

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

                    Spacer(modifier = Modifier.height(8.dp))

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
                        isError = validationError
                            ?.contains("Train Number") == true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = trainName,
                        onValueChange = { },
                        label = {
                            Text("Train Name")
                        },
                        placeholder = {
                            Text("Auto-populated")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        isError = validationError
                            ?.contains("Train Name") == true,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = classType,
                        onValueChange = {
                            viewModel.updateClassType(it)
                        },
                        label = {
                            Text("Class Type")
                        },
                        placeholder = {
                            Text("e.g., 3A")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = validationError
                            ?.contains("Class Type") == true,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // -------------------------------------------------
                    // QUOTA
                    // -------------------------------------------------

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
                                text = quota?.name ?: "Select Quota"
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
                                        viewModel.updateQuota(q)
                                        quotaExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // -------------------------------------------------
                    // STATIONS
                    // -------------------------------------------------

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedTextField(
                            value = fromStation,
                            onValueChange = {
                                viewModel.updateFromStation(it)
                            },
                            label = {
                                Text("From Station Code")
                            },
                            placeholder = {
                                Text("e.g., NDLS")
                            },
                            modifier = Modifier.weight(1f),
                            isError = validationError
                                ?.contains("From Station") == true,
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        OutlinedTextField(
                            value = toStation,
                            onValueChange = {
                                viewModel.updateToStation(it)
                            },
                            label = {
                                Text("To Station Code")
                            },
                            placeholder = {
                                Text("e.g., MUMBAI")
                            },
                            modifier = Modifier.weight(1f),
                            isError = validationError
                                ?.contains("To Station") == true,
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // -------------------------------------------------
                    // JOURNEY DATE
                    // -------------------------------------------------

                    OutlinedTextField(
                        value = journeyDate,
                        onValueChange = {
                            viewModel.updateJourneyDate(it)
                        },
                        label = {
                            Text("Journey Date (YYYY-MM-DD)")
                        },
                        placeholder = {
                            Text("e.g., 2026-08-10")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = validationError
                            ?.contains("Date") == true,
                        singleLine = true
                    )

                    if (validationError != null) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = validationError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // ---------------------------------------------------------
            // PASSENGER DATA
            // ---------------------------------------------------------

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

            // ---------------------------------------------------------
            // AUTOMATION STATUS
            // ---------------------------------------------------------

            Text(
                text = "Automation Status: $statusText",
                fontWeight = FontWeight.Bold,
                color = when (currentWorkflowState) {

                    is WorkflowState.RUNNING ->
                        MaterialTheme.colorScheme.primary

                    is WorkflowState.ERROR ->
                        MaterialTheme.colorScheme.error

                    else ->
                        MaterialTheme.colorScheme.onBackground
                }
            )

            // Extra bottom breathing room for the scrollable content.
            // The actual action button is owned by Scaffold.bottomBar.
            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }

    // -------------------------------------------------------------
    // PASSENGER DIALOG
    // -------------------------------------------------------------

    if (showPassengerDialog) {

        AlertDialog(
            onDismissRequest = {
                showPassengerDialog = false
            },

            title = {
                Text(
                    text = "Add Passenger Data"
                )
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = passengerName,
                        onValueChange = {
                            viewModel.updatePassengerName(it)
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
                            viewModel.updatePassengerAge(it)
                        },
                        label = {
                            Text("Age")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
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
                            Text(passengerGender)
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
                                        viewModel.updatePassengerGender(
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
                            viewModel.updatePassengerMobile(it)
                        },
                        label = {
                            Text("Mobile (10 digits)")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        )
                    )
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
