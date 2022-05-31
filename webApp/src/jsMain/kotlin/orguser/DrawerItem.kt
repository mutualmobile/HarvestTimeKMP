package orguser

import com.mutualmobile.harvestKmp.data.network.Constants
import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.Routes
import com.mutualmobile.harvestKmp.di.SharedComponent
import harvest.JSLoginScreen
import kotlinx.browser.window
import react.FC
import react.Props
import react.useMemo

data class DrawerItem(
    val key: String,
    val name: String,
    val Component: FC<Props>,
)

typealias DrawerItems = Iterable<DrawerItem>

fun useDrawerItems(): DrawerItems {
    SharedComponent().provideHarvestUserLocal().getUser()?.role?.let { role ->
        when (role) {
            UserRole.ORG_ADMIN.role -> {
                return useMemo(callback = {
                    setOf(
                        DrawerItem(Routes.Screen.ORG_USERS, "Users", JsOrgUsersScreen),
                        DrawerItem(Routes.Screen.ORG_PROJECTS, "Projects", JsOrgProjectsScreen),
                        DrawerItem(Routes.Screen.SETTINGS, "Settings", JsSettingsScreen)
                    )
                })
            }
            UserRole.ORG_USER.role -> {
                return useMemo(callback = {
                    setOf(
                        DrawerItem(Routes.Screen.ORG_PROJECTS, "Projects", JsOrgProjectsScreen),
                        DrawerItem(Routes.Screen.ORG_TIME, "Time", JsTimeLoggingScreen),
                        DrawerItem(Routes.Screen.SETTINGS, "Settings", JsSettingsScreen)
                    )
                })
            }
            else -> {
                window.alert("we should always know the role of the person!")
                throw RuntimeException("we should always know the role of the person!")
            }
        }
    }
    window.alert("we should always know the role of the person!")
    throw RuntimeException("we should always know the role of the person!")
}
