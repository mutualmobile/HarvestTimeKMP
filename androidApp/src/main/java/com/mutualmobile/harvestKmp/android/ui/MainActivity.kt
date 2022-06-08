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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.findWorkspaceScreen.FindWorkspaceScreen
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.LandingScreen
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.LoginScreen
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.NewEntryScreen
import com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen.ForgotPasswordScreen
import com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen.OnBoardingScreen
import com.mutualmobile.harvestKmp.android.ui.screens.password.ChangePasswordScreen
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.NewOrgSignUpScreen
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.SignUpScreen
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme
import com.mutualmobile.harvestKmp.android.ui.utils.SetupSystemUiController
import com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.SettingsScreen

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
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ScreenList.OnBoardingScreen(),
                    ) {
                        composable(ScreenList.OnBoardingScreen()) {
                            OnBoardingScreen(navController = navController)
                        }
                        composable(ScreenList.ExistingOrgSignUpScreen()) {
                            SignUpScreen(navController = navController)
                        }
                        composable(ScreenList.NewOrgSignUpScreen()) {
                            NewOrgSignUpScreen(navController = navController)
                        }
                        composable(ScreenList.LoginScreen()) {
                            LoginScreen(navController = navController)
                        }
                        composable(ScreenList.LandingScreen()) {
                            LandingScreen(navController = navController)
                        }
                        composable(ScreenList.FindWorkspaceScreen()) {
                            FindWorkspaceScreen(navController = navController)
                        }
                        composable(ScreenList.NewEntryScreen()) {
                            NewEntryScreen(navController = navController)
                        }
                        composable(ScreenList.ForgotPasswordScreen()) {
                            ForgotPasswordScreen(navController = navController)
                        }
                        composable(ScreenList.ChangePasswordScreen()) {
                            ChangePasswordScreen(navController = navController)
                        }
                        composable(ScreenList.SettingsScreen()) {
                            SettingsScreen(navController = navController)
                        }
                    }
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