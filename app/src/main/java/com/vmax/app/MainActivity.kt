package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the Compose entry point
        setContent {
            VMAXTheme {
                // Empty Scaffold/Placeholder as per Blueprint rule
                // (UI content is not invented here, only entry point is set)
            }
        }
    }
}
