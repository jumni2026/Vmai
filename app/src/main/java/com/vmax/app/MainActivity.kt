package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Explicitly calling the extension function from the package
        setContent {
            VMAXApp()
        }
    }
}

// Declaring VMAXApp as a Composable function to satisfy the compiler
@Composable
fun VMAXApp() {
    // Using the Theme defined in Theme.kt
    VMAXTheme {
        // Placeholder for UI (Will be built in next stages per Blueprint)
    }
}
