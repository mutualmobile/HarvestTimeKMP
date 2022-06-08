package orguser

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import kotlinx.browser.window
import orgadmin.JsProjectAssignScreen
import orguser.timelogging.JsTimeLoggingScreen
import react.FC
import react.Props

data class DrawerItem(
    val key: String,
    val name: String,
    val Component: FC<Props>,
)

typealias DrawerItems = Iterable<DrawerItem>

fun drawerItems(role: String): DrawerItems {
    when (role) {
        UserRole.ORG_ADMIN.role -> {
            return setOf(
                DrawerItem(HarvestRoutes.Screen.ORG_USERS, "Users", JsOrgUsersScreen),
                DrawerItem(HarvestRoutes.Screen.ORG_PROJECTS, "Projects", JsOrgProjectsScreen),
                DrawerItem(HarvestRoutes.Screen.ASSIGN_PROJECT,"Assignments",JsProjectAssignScreen),
                DrawerItem(HarvestRoutes.Screen.SETTINGS, "Settings", JsSettingsScreen)
            )
        }
        UserRole.ORG_USER.role -> {
            return setOf(
                DrawerItem(HarvestRoutes.Screen.ORG_TIME, "Time", JsTimeLoggingScreen),
                DrawerItem(HarvestRoutes.Screen.USER_REPORT, "Reports", JSUserReportScreen),
                DrawerItem(HarvestRoutes.Screen.SETTINGS, "Settings", JsSettingsScreen)
            )
        }
        else -> {
            window.alert("we should always know the role of the person!")
            throw RuntimeException("we should always know the role of the person!")
        }
    }
}
