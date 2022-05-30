package orguser

import com.mutualmobile.harvestKmp.data.network.Constants
import com.mutualmobile.harvestKmp.datamodel.Routes
import com.mutualmobile.harvestKmp.di.SharedComponent
import harvest.JSLoginScreen
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
    SharedComponent().provideHarvestUserLocal().getUser().role?.let { role ->
        when (role) {
            Constants.USER_ORG_ADMIN -> {
                return useMemo(callback = {
                    setOf(
                        DrawerItem(Routes.Screen.ORG_USERS, "Users", JsOrgUsersScreen),
                        DrawerItem(Routes.Screen.ORG_PROJECTS, "Projects", JsOrgProjectsScreen),
                        DrawerItem(Routes.Screen.SETTINGS, "Settings", JsSettingsScreen)
                    )
                })
            }
            Constants.USER_ROLE_ORG_USER -> {
                return useMemo(callback = {
                    setOf(
                        DrawerItem(Routes.Screen.ORG_PROJECTS, "Projects", JsOrgProjectsScreen),
                        DrawerItem(Routes.Screen.ORG_TIME, "Time", JsTimeLoggingScreen),
                        DrawerItem(Routes.Screen.SETTINGS, "Settings", JsSettingsScreen)
                    )
                })
            }
            else -> {
                throw RuntimeException("we should always know the role of the person!")
            }
        }
    }

    throw RuntimeException("we should always know the role of the person!")
}
