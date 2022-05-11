package com.mutualmobile.harvestKmp.android.ui.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Activity.SetupSystemUiController() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        systemUiController.setSystemBarsColor(
            color = Color.Transparent.copy(alpha = 0.25f),
            darkIcons = false
        )
    }
}