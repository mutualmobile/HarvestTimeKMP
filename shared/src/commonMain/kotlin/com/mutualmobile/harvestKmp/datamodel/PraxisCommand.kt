package com.mutualmobile.harvestKmp.datamodel

open class PraxisCommand

const val BROWSER_SCREEN_ROUTE_SEPARATOR = "/"
data class NavigationPraxisCommand(val screen: String, val route: String) : PraxisCommand()
data class ModalPraxisCommand(val title: String, val message: String) : PraxisCommand()

object Routes{
    object Screen{
        const val TRENDING_UI = "trendingui"
    }
}