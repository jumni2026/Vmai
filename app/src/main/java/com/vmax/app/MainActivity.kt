package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VMAXApp()
        }
    }
}

// Adding @Composable annotation ensures it's recognized inside setContent
@Composable
fun VMAXApp() {
    VMAXTheme {
        // Empty Entry Point - UI will be defined strictly as per Blueprint later
    }
}
