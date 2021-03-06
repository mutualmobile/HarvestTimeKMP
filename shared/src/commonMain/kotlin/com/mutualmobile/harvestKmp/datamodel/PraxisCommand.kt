package com.mutualmobile.harvestKmp.datamodel

open class PraxisCommand

const val BROWSER_SCREEN_ROUTE_SEPARATOR = "/"
const val BROWSER_QUERY = "?"
const val BROWSER_AND = "&"

data class NavigationPraxisCommand(val screen: String, val route: String? = null) : PraxisCommand()
data class ModalPraxisCommand(val title: String, val message: String) : PraxisCommand()

object HarvestRoutes {
    object Keys {
        const val orgIdentifier = "orgIdentifier"
        const val orgId = "orgId"
        const val id = "id"
    }

    object Screen {
        const val FORGOT_PASSWORD = "forgot-password"
        const val RESET_PASSWORD = "resetPassword"
        const val CHANGE_PASSWORD = "change-password"
        const val LOGIN = "login"
        const val LOGIN_WITH_ORG_ID_IDENTIFIER = LOGIN
            .plus(BROWSER_QUERY)
            .plus("${Keys.orgId}={${Keys.orgId}}")
            .plus(BROWSER_AND)
            .plus("${Keys.orgIdentifier}={${Keys.orgIdentifier}}")
        const val SIGNUP = "signup"
        const val NEW_ORG_SIGNUP = "new_org_signup"
        const val ORG_USERS = "users"
        const val ORG_PROJECTS = "projects"
        const val ASSIGN_PROJECT = "assign-projects"
        const val ORG_TIME = "time-log-screen"
        const val SETTINGS = "settings"
        const val USER_REPORT = "user-reports"
        const val ORG_USER_DASHBOARD = "user-dashboard"
        const val LIST_USERS_PROJECT = "list-user-project"
        const val LIST_PROJECTS_USER = "list-projects-user"
        const val ON_BOARDING = "on_boarding"
        const val FIND_WORKSPACE = "find-workspace"
        const val WORK_ENTRY = "work-entry"
        const val SELECT_WORK_TYPE = "select-work-type"

        fun String.listUsersWithProjectId(projectId: String?): String {
            return this.plus(
                BROWSER_QUERY + "${Keys.id}=${projectId ?: ""}"
            )
        }

        fun String.listProjectsAssignedToUser(userId: String?): String {
            return this.plus(
                BROWSER_QUERY + "${Keys.id}=${userId ?: ""}"
            )
        }

        fun String.withOrgId(identifier: String?, id: String?): String {
            return this.plus(
                BROWSER_QUERY + "${Keys.orgIdentifier}=${identifier ?: ""}" + BROWSER_AND +
                        "${Keys.orgId}=${id ?: ""}"
            )
        }
    }
}