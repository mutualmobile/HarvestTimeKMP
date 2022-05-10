package com.mutualmobile.harvestKmp.android.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.LandingScreen
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            HarvestKmpTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent.copy(alpha = 0.25f),
                        darkIcons = false
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LandingScreen()
                }
            }
        }
    }

    private fun setupSplashScreen() {
        installSplashScreen().setOnExitAnimationListener { splashScreenView ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0f
            )
            with(fadeOut) {
                interpolator = LinearInterpolator()
                duration = 200L
                doOnEnd { splashScreenView.remove() }
                start()
            }
        }
    }
}