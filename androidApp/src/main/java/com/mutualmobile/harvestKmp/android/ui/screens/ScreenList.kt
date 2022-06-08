package com.mutualmobile.harvestKmp.android.ui.screens

sealed class ScreenList(private val route: String) {
    object OnBoardingScreen: ScreenList("onBoardingScreen")
    object LoginScreen: ScreenList("loginScreen")
    object LandingScreen: ScreenList("landingScreen")
    object ExistingOrgSignUpScreen: ScreenList("existingOrgSignUpScreen")
    object NewOrgSignUpScreen: ScreenList("newOrgSignUpScreen")
    object FindWorkspaceScreen: ScreenList("findWorkspaceScreen")
    object NewEntryScreen: ScreenList("newEntryScreen")
    object ForgotPasswordScreen: ScreenList("forgotPasswordScreen")
    object ChangePasswordScreen: ScreenList("changePasswordScreen")
    object SettingsScreen: ScreenList("settingsScreen")
    operator fun invoke() = route
}