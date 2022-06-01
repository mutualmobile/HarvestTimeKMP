package com.mutualmobile.harvestKmp.datamodel

open class PraxisCommand

const val BROWSER_SCREEN_ROUTE_SEPARATOR = "/"
const val BROWSER_QUERY = "?"
const val BROWSER_AND = "&"

data class NavigationPraxisCommand(val screen: String, val route: String? = null) : PraxisCommand()
data class ModalPraxisCommand(val title: String, val message: String) : PraxisCommand()

object Routes {
    object Keys {
        const val orgIdentifier = "orgIdentifier"
        const val orgId = "orgId"
    }

    object Screen {
        const val TRENDING_UI = "trendingui"
        const val FORGOT_PASSWORD = "forgot-password"
        const val RESET_PASSWORD = "resetPassword"
        const val CHANGE_PASSWORD = "change-password"
        const val LOGIN = "login"
        const val SIGNUP = "signup"
        const val ORG_USERS = "users"
        const val ORG_PROJECTS = "projects"
        const val ORG_TIME = "time-log-screen"
        const val SETTINGS = "settings"
        const val ORG_USER_DASHBOARD = "user-dashboard"
        const val ORG_USER_FETCH = "user-data-fetch"
        const val CREATE_PROJECT = "create-edit-project"

        fun String.withOrgId(identifier: String?, id: String?): String {
            return this.plus(
                BROWSER_QUERY + "${Keys.orgIdentifier}=${identifier ?: ""}" + BROWSER_AND +
                        "${Keys.orgId}=${id ?: ""}"
            )
        }
    }
}