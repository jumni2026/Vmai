package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
// Re-ensuring the correct import for setContent
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Evidence-based fix: Explicitly wrapping VMAXTheme in a Composable context
        setContent {
            // Simple Composable lambda to satisfy the compiler context rule
            @Composable
            fun MainScreen() {
                VMAXTheme {
                    // Placeholder strictly following Blueprint
                }
            }
            MainScreen()
        }
    }
}
