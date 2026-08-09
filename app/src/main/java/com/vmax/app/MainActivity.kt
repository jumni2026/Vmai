package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState  // ✅ Correct import
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vmax.workflow.WorkflowController
import com.vmax.workflow.WorkflowState
import com.vmax.model.Train
import com.vmax.model.Station
import com.vmax.model.Passenger
import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    val workflowController = WorkflowController.getInstance()
    val workflowState by workflowController.state.collectAsState()

    // UI State
    var showPassengerDialog by remember { mutableStateOf(false) }
    var passengerName by remember { mutableStateOf("") }
    var passengerAge by remember { mutableStateOf("") }
    var passengerGender by remember { mutableStateOf("MALE") }
    var passengerMobile by remember { mutableStateOf("") }
    var genderMenuExpanded by remember { mutableStateOf(false) }

    var trainNumber by remember { mutableStateOf("") }
    var trainName by remember { mutableStateOf("") }
    var classType by remember { mutableStateOf("") }
    var quota by remember { mutableStateOf("") }
    var fromStationCode by remember { mutableStateOf("") }
    var toStationCode by remember { mutableStateOf("") }
    var journeyDate by remember { mutableStateOf("") }

    // UI Validation Errors
    var validationError by remember { mutableStateOf<String?>(null) }

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
        Text(text = "VERSION: 2.6 FINAL 🔒", fontSize = 12.sp)
        
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
                    onValueChange = { trainNumber = it },
                    label = { Text("Train Number") },
                    placeholder = { Text("e.g., 20503") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = validationError?.contains("Train Number") == true
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = trainName,
                    onValueChange = { trainName = it },
                    label = { Text("Train Name") },
                    placeholder = { Text("e.g., RAJDHANI EXP") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = validationError?.contains("Train Name") == true
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = classType,
                    onValueChange = { classType = it },
                    label = { Text("Class Type") },
                    placeholder = { Text("e.g., 3A") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = validationError?.contains("Class Type") == true
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = quota,
                    onValueChange = { quota = it },
                    label = { Text("Quota") },
                    placeholder = { Text("e.g., GENERAL") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = validationError?.contains("Quota") == true
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fromStationCode,
                        onValueChange = { fromStationCode = it },
                        label = { Text("From Station Code") },
                        placeholder = { Text("e.g., NDLS") },
                        modifier = Modifier.weight(1f),
                        isError = validationError?.contains("From Station") == true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = toStationCode,
                        onValueChange = { toStationCode = it },
                        label = { Text("To Station Code") },
                        placeholder = { Text("e.g., MUMBAI") },
                        modifier = Modifier.weight(1f),
                        isError = validationError?.contains("To Station") == true
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = journeyDate,
                    onValueChange = { journeyDate = it },
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

        // Passenger Data Notebook Card
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

        // Workflow State Observation
        // ✅ FIX #2: Explicit local variable to handle Kotlin smart-cast
        val currentWorkflowState = workflowState
        val statusText = when (currentWorkflowState) {
            is WorkflowState.CONFIGURED -> "CONFIGURED (Waiting for Engine)"
            is WorkflowState.RUNNING -> "RUNNING (Target: ${trainNumber.takeIf { it.isNotBlank() } ?: "N/A"})"
            is WorkflowState.ERROR -> "ERROR: ${currentWorkflowState.reason}"
            else -> "IDLE"
        }
        
        Text(
            text = "Automation Status: $statusText", 
            fontWeight = FontWeight.Bold,
            color = when (currentWorkflowState) {
                is WorkflowState.RUNNING -> MaterialTheme.colorScheme.primary
                is WorkflowState.ERROR -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onBackground
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        val isWorkflowActive = currentWorkflowState is WorkflowState.RUNNING || currentWorkflowState is WorkflowState.CONFIGURED
        Button(
            onClick = {
                validationError = null

                // Train Number Validation
                if (trainNumber.isBlank()) {
                    validationError = "Train Number is required."
                    return@Button
                }
                if (!trainNumber.matches(Regex("^\\d{4,5}$"))) {
                    validationError = "Train Number must be 4 or 5 digits (e.g., 20503)."
                    return@Button
                }

                // Train Name Validation
                if (trainName.isBlank()) {
                    validationError = "Train Name is required."
                    return@Button
                }

                // Class Type Validation
                if (classType.isBlank()) {
                    validationError = "Class Type is required."
                    return@Button
                }
                if (!classType.matches(Regex("^[1-3][A-Z]$"))) {
                    validationError = "Class Type must be valid (e.g., 3A, 2A, 1A, SL, etc.)."
                    return@Button
                }

                // Quota Validation
                if (quota.isBlank()) {
                    validationError = "Quota is required."
                    return@Button
                }

                // Station Code Validation
                if (fromStationCode.isBlank()) {
                    validationError = "From Station Code is required."
                    return@Button
                }
                if (!fromStationCode.matches(Regex("^[A-Z]{4}$"))) {
                    validationError = "From Station Code must be 4 uppercase letters (e.g., NDLS)."
                    return@Button
                }
                if (toStationCode.isBlank()) {
                    validationError = "To Station Code is required."
                    return@Button
                }
                if (!toStationCode.matches(Regex("^[A-Z]{4}$"))) {
                    validationError = "To Station Code must be 4 uppercase letters (e.g., MUMBAI)."
                    return@Button
                }

                // Journey Date Validation
                if (journeyDate.isBlank()) {
                    validationError = "Journey Date is required."
                    return@Button
                }
                try {
                    LocalDateTime.parse(journeyDate + "T00:00:00")
                } catch (e: Exception) {
                    validationError = "Date must be in YYYY-MM-DD format (e.g., 2026-08-10)."
                    return@Button
                }

                // Passenger Age Validation
                if (passengerName.isBlank()) {
                    validationError = "Passenger Name is required."
                    return@Button
                }
                val ageInt = passengerAge.toIntOrNull()
                if (ageInt == null || ageInt !in 1..120) {
                    validationError = "Valid Age (1-120) is required."
                    return@Button
                }

                // Mobile Validation
                if (passengerMobile.isNotBlank() && !passengerMobile.matches(Regex("^[6-9]\\d{9}$"))) {
                    validationError = "Mobile must be exactly 10 digits starting with 6-9."
                    return@Button
                }

                // ✅ Model Construction (Strictly from Evidence)
                val train = Train(
                    number = trainNumber,
                    name = trainName,
                    classType = classType,
                    quota = quota
                )
                val fromStation = Station(fromStationCode, fromStationCode)
                val toStation = Station(toStationCode, toStationCode)

                val passenger = Passenger(
                    name = passengerName,
                    age = ageInt,
                    gender = passengerGender,
                    mobile = passengerMobile.takeIf { it.isNotBlank() }
                )

                if (isWorkflowActive) {
                    workflowController.stop()
                } else {
                    workflowController.start(
                        bookingRequest = BookingRequest(
                            train = train,
                            fromStation = fromStation,
                            toStation = toStation,
                            date = journeyDate,
                            passengers = listOf(passenger),
                            quota = quota
                        ),
                        passengerProfile = PassengerProfile(
                            profileId = "PROFILE_001",
                            passengers = listOf(passenger),
                            createdTime = LocalDateTime.now(),
                            updatedTime = LocalDateTime.now()
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isWorkflowActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = if (isWorkflowActive) "STOP WORKFLOW ENGINE" else "CONFIGURE & START ENGINE", 
                fontSize = 16.sp, 
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Passenger Input Pop-up Dialog
    if (showPassengerDialog) {
        AlertDialog(
            onDismissRequest = { showPassengerDialog = false },
            title = { Text(text = "Add Passenger Data") },
            text = {
                Column {
                    OutlinedTextField(
                        value = passengerName,
                        onValueChange = { passengerName = it },
                        label = { Text("Passenger Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passengerAge,
                        onValueChange = { passengerAge = it },
                        label = { Text("Age") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Gender", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { genderMenuExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(passengerGender)
                        }
                        DropdownMenu(
                            expanded = genderMenuExpanded,
                            onDismissRequest = { genderMenuExpanded = false }
                        ) {
                            listOf("MALE", "FEMALE", "OTHER").forEach { gender ->
                                DropdownMenuItem(
                                    text = { Text(gender) },
                                    onClick = {
                                        passengerGender = gender
                                        genderMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passengerMobile,
                        onValueChange = { passengerMobile = it },
                        label = { Text("Mobile (10 digits)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (passengerName.isNotBlank() && passengerAge.isNotBlank()) {
                            showPassengerDialog = false
                        }
                    }
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
