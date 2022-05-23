package com.mutualmobile.harvestKmp.android.ui.screens

sealed class ScreenList(val route: String) {
    object LoginScreen: ScreenList("loginScreen")
    object LandingScreen: ScreenList("landingScreen")
    object ExistingOrgSignUpScreen: ScreenList("existingOrgSignUpScreen")
    operator fun invoke() = route
}