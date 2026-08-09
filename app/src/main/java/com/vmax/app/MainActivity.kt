package com.vmax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * VMAX Enterprise v2.6
 *
 * Stage 2 — Compose Entry Point
 * File 31 — MainActivity
 *
 * Main entry point for the VMAX Enterprise application.
 * Platform: Android.
 * No business logic.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VMAXTheme {
                // Stage 2 — Compose entry point only.
            }
        }
    }
}
