package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vmax.model.*

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

    val berthPreference by viewModel.berthPreference.collectAsState()
    val mealPreference by viewModel.mealPreference.collectAsState()
    val concession by viewModel.concession.collectAsState()
    val bedRoll by viewModel.bedRoll.collectAsState()
    val children by viewModel.children.collectAsState()
    val bookingOption by viewModel.bookingOption.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()

    val validationError by viewModel.validationError.collectAsState()
    val workflowState by viewModel.workflowState.collectAsState()

    var showPassengerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "VMAX ENTERPRISE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = "VERSION: 2.6.1 FINAL 🔒", fontSize = 12.sp)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Target Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "🎯 Target Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = trainNumber,
                    onValueChange = { viewModel.updateTrainNumber(it) },
                    label = { Text("Train Number") },
                    placeholder = { Text("e.g., 20503") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = validationError?.contains("Train Number") == true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = trainName,
                    onValueChange = { },
                    label = { Text("Train Name") },
                    placeholder = { Text("Auto-populated") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    isError = validationError?.contains("Train Name") == true
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = classType,
                    onValueChange = { viewModel.updateClassType(it) },
                    label = { Text("Class Type") },
                    placeholder = { Text("e.g., 3A") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = validationError?.contains("Class Type") == true
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                // Quota Dropdown
                Text("Quota", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                var quotaExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { quotaExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(quota?.name ?: "Select Quota")
                    }
                    DropdownMenu(
                        expanded = quotaExpanded,
                        onDismissRequest = { quotaExpanded = false }
                    ) {
                        Quota.values().forEach { q ->
                            DropdownMenuItem(
                                text = { Text(q.name) },
                                onClick = {
                                    viewModel.updateQuota(q)
                                    quotaExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fromStation,
                        onValueChange = { viewModel.updateFromStation(it) },
                        label = { Text("From Station Code") },
                        placeholder = { Text("e.g., NDLS") },
                        modifier = Modifier.weight(1f),
                        isError = validationError?.contains("From Station") == true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = toStation,
                        onValueChange = { viewModel.updateToStation(it) },
                        label = { Text("To Station Code") },
                        placeholder = { Text("e.g., MUMBAI") },
                        modifier = Modifier.weight(1f),
                        isError = validationError?.contains("To Station") == true
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = journeyDate,
                    onValueChange = { viewModel.updateJourneyDate(it) },
                    label = { Text("Journey Date (YYYY-MM-DD)") },
                    placeholder = { Text("e.g., 2026-08-10") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = validationError?.contains("Date") == true
                )
                if (validationError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = validationError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Passenger Data Notebook
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "👤 Passenger Data Notebook", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (passengerName.isNotBlank()) "Status: Configured ($passengerName, $passengerAge Yrs)" 
                           else "Status: Pop-up Interface Ready"
                )
                
                Button(
                    onClick = { showPassengerDialog = true },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Configure via Pop-up")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        val statusText = when (workflowState) {
            is WorkflowState.CONFIGURED -> "CONFIGURED (Waiting for Engine)"
            is WorkflowState.RUNNING -> "RUNNING (Target: ${trainNumber.takeIf { it.isNotBlank() } ?: "N/A"})"
            is WorkflowState.ERROR -> "ERROR: ${workflowState.reason}"
            else -> "IDLE"
        }
        
        Text(
            text = "Automation Status: $statusText", 
            fontWeight = FontWeight.Bold,
            color = when (workflowState) {
                is WorkflowState.RUNNING -> MaterialTheme.colorScheme.primary
                is WorkflowState.ERROR -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onBackground
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        
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
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.isWorkflowActive()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = if (viewModel.isWorkflowActive()) "STOP WORKFLOW ENGINE" else "CONFIGURE & START ENGINE", 
                fontSize = 16.sp, 
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Passenger Pop-up Dialog
    if (showPassengerDialog) {
        AlertDialog(
            onDismissRequest = { showPassengerDialog = false },
            title = { Text(text = "Add Passenger Data") },
            text = {
                Column {
                    OutlinedTextField(
                        value = passengerName,
                        onValueChange = { viewModel.updatePassengerName(it) },
                        label = { Text("Passenger Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passengerAge,
                        onValueChange = { viewModel.updatePassengerAge(it) },
                        label = { Text("Age") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Gender", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    // Gender Dropdown
                    var genderExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { genderExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(passengerGender)
                        }
                        DropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            listOf("MALE", "FEMALE", "OTHER").forEach { gender ->
                                DropdownMenuItem(
                                    text = { Text(gender) },
                                    onClick = {
                                        viewModel.updatePassengerGender(gender)
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passengerMobile,
                        onValueChange = { viewModel.updatePassengerMobile(it) },
                        label = { Text("Mobile (10 digits)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPassengerDialog = false }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPassengerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
