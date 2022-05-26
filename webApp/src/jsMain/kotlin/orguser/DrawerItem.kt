package orguser

import com.mutualmobile.harvestKmp.datamodel.Routes
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

fun useOrgUserDrawerItems(): DrawerItems {
    return useMemo(callback = {
        setOf(
            DrawerItem(Routes.Screen.ORG_USERS, "Users", JsOrgUsersScreen),
            DrawerItem(Routes.Screen.ORG_PROJECTS, "Projects", JsOrgProjectsScreen),
            DrawerItem(Routes.Screen.SETTINGS, "Settings", JsSettingsScreen)
        )
    })
}
