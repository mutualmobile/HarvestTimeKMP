package com.mutualmobile.harvestKmp.datamodel

open class PraxisCommand

const val BROWSER_SCREEN_ROUTE_SEPARATOR = "/"
data class NavigationPraxisCommand(val screen: String, val route: String) : PraxisCommand()
data class ModalPraxisCommand(val title: String, val message: String) : PraxisCommand()

object Routes{
    object Screen{
        const val TRENDING_UI = "trendingui"
        const val FORGOT_PASSWORD = "forgotPassword"
        const val RESET_PASSWORD = "resetPassword"
        const val CHANGE_PASSWORD = "changePassword"
        const val LOGIN = "login"
        const val SIGNUP = "signup"
    }
}