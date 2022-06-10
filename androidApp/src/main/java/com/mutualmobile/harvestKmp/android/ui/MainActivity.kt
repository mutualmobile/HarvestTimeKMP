package com.mutualmobile.harvestKmp.android.ui

//noinspection SuspiciousImport
import android.R
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mutualmobile.harvestKmp.android.ui.screens.findWorkspaceScreen.FindWorkspaceScreen
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.LandingScreen
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.LoginScreen
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.NewEntryScreen
import com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen.OnBoardingScreen
import com.mutualmobile.harvestKmp.android.ui.screens.password.ChangePasswordScreen
import com.mutualmobile.harvestKmp.android.ui.screens.password.ForgotPasswordScreen
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.ProjectScreen
import com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.SettingsScreen
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.NewOrgSignUpScreen
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.SignUpScreen
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme
import com.mutualmobile.harvestKmp.android.ui.utils.SetupSystemUiController
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel

class MainActivity : ComponentActivity() {
    var getUserState: DataState by mutableStateOf(EmptyState)

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
                    remember { mutableStateOf(
                        GetUserDataModel { newState ->
                            getUserState = newState
                        }.activate()
                    ) }

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination =
                        if (getUserState is SuccessState<*>) {
                            HarvestRoutes.Screen.DASHBOARD_WITH_ORG_ID_IDENTIFIER
                        } else {
                            HarvestRoutes.Screen.ON_BOARDING
                        },
                    ) {
                        composable(HarvestRoutes.Screen.ON_BOARDING) {
                            OnBoardingScreen(navController = navController)
                        }
                        composable(HarvestRoutes.Screen.SIGNUP) {
                            SignUpScreen(navController = navController)
                        }
                        composable(HarvestRoutes.Screen.NEW_ORG_SIGNUP) {
                            NewOrgSignUpScreen(navController = navController)
                        }
                        composable(
                            HarvestRoutes.Screen.LOGIN_WITH_ORG_ID_IDENTIFIER,
                            arguments = listOf(
                                navArgument(HarvestRoutes.Keys.orgId) { nullable = true },
                                navArgument(HarvestRoutes.Keys.orgIdentifier) { nullable = true },
                            ),
                        ) { backStackEntry ->
                            LoginScreen(
                                navController = navController,
                                orgIdentifier = backStackEntry
                                    .arguments?.getString(HarvestRoutes.Keys.orgIdentifier)
                            )
                        }
                        composable(
                            HarvestRoutes.Screen.DASHBOARD_WITH_ORG_ID_IDENTIFIER,
                            arguments = listOf(
                                navArgument(HarvestRoutes.Keys.orgId) { nullable = true },
                                navArgument(HarvestRoutes.Keys.orgIdentifier) { nullable = true },
                            ),
                        ) { backStackEntry ->
                            LandingScreen(
                                navController = navController,
                                orgIdentifier = backStackEntry
                                    .arguments?.getString(HarvestRoutes.Keys.orgIdentifier)
                            )
                        }
                        composable(HarvestRoutes.Screen.FIND_WORKSPACE) {
                            FindWorkspaceScreen(navController = navController)
                        }
                        composable(HarvestRoutes.Screen.ORG_PROJECTS) {
                            ProjectScreen(navController=navController)
                        }
                        composable(HarvestRoutes.Screen.WORK_ENTRY) {
                            NewEntryScreen(navController=navController)
                        }
                        composable(HarvestRoutes.Screen.SETTINGS) {
                            SettingsScreen(navController = navController)
                        }
                        composable(HarvestRoutes.Screen.FORGOT_PASSWORD) {
                            ForgotPasswordScreen(navController = navController)
                        }
                        composable(HarvestRoutes.Screen.CHANGE_PASSWORD) {
                            ChangePasswordScreen(navController = navController)
                        }
                        composable(HarvestRoutes.Screen.SETTINGS) {
                            SettingsScreen(navController = navController)
                        }
                    }
                }
            }
        }
        removeSplashScreen()
    }

    private fun removeSplashScreen() {
        // Set up an OnPreDrawListener to the root view.
        val content: View = findViewById(R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (getUserState !is EmptyState && getUserState !is LoadingState) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )
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