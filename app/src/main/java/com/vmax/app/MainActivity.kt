package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VMAXTheme { // आपकी मौजूदा थीम बरकरार रखी गई है
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // हैडर
        Text(
            text = "VMAX ENTERPRISE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = "VERSION: 2.6 FINAL 🔒", fontSize = 12.sp)
        
        Spacer(modifier = Modifier.height(32.dp))

        // टार्गेट कॉन्फ़िगरेशन कार्ड
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "🎯 Target Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Train Number: 20503 (MANDATORY EXACT)")
                Text(text = "Class Type: 3A (MANDATORY EXACT)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // पैसेंजर डेटा नोटबुक कार्ड
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "👤 Passenger Data Notebook", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Status: Pop-up Interface Ready")
                
                Button(
                    onClick = { /* TODO: Trigger Passenger Data Pop-up */ },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Configure via Pop-up")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ऑटोमेशन स्टेटस
        Text(text = "Automation Status: IDLE", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        
        // फुल ऑटोमेशन इंजन स्टार्टर
        Button(
            onClick = { /* TODO: Connect to Workflow Controller */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("START FULL AUTOMATION ENGINE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
