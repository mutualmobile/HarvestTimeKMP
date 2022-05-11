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
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.LandingScreen
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme
import com.mutualmobile.harvestKmp.android.ui.utils.SetupSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            HarvestKmpTheme {
                SetupSystemUiController()
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