package com.mutualmobile.harvestKmp.android.ui.screens

sealed class ScreenList(private val route: String) {
    object Keys {
        const val orgIdentifier = "orgIdentifier"
    }
    object OnBoardingScreen: ScreenList("onBoardingScreen")
    object LoginScreen: ScreenList("loginScreen/{${Keys.orgIdentifier}}") {
        fun orgIdentifierRoute(orgIdentifier: String) = "loginScreen/$orgIdentifier"
    }
    object LandingScreen: ScreenList("landingScreen/{${Keys.orgIdentifier}}") {
        fun orgIdentifierRoute(orgIdentifier: String) = "landingScreen/$orgIdentifier"
    }
    object ExistingOrgSignUpScreen: ScreenList("existingOrgSignUpScreen")
    object NewOrgSignUpScreen: ScreenList("newOrgSignUpScreen")
    object FindWorkspaceScreen: ScreenList("findWorkspaceScreen")
    object SettingsScreen: ScreenList("settingsScreen")
    object NewEntryScreen: ScreenList("newEntryScreen")
    operator fun invoke() = route
}