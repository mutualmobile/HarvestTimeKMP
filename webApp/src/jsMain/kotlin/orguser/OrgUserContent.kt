package orguser

import com.mutualmobile.harvestKmp.datamodel.BROWSER_SCREEN_ROUTE_SEPARATOR
import csstype.px
import mui.material.Typography
import mui.system.Box
import mui.system.sx
import orguser.structure.Area
import project.JSOrgProjectUsersList
import project.JSUserProjectList
import react.*
import react.dom.html.ReactHTML.main
import react.router.Outlet
import react.router.Route
import react.router.Routes

private val DEFAULT_PADDING = 30.px

val OrgUserContent = VFC {
    val drawerItems = useContext(DrawerItemsContext)
    Routes {
        Route {
            path = "/"
            element = Box.create {
                component = main
                sx {
                    gridArea = Area.Content
                    padding = DEFAULT_PADDING
                }

                Outlet()
            }

            drawerItems.forEachIndexed { i, (key, _, Component) ->
                Route {
                    index = i == 0
                    path = key
                    element = Component.create()
                }
            }

            Route {
                this.path = com.mutualmobile.harvestKmp.datamodel.Routes.Screen.LIST_USERS_PROJECT
                this.element = JSOrgProjectUsersList.create()
            }
            Route {
                this.path = com.mutualmobile.harvestKmp.datamodel.Routes.Screen.LIST_PROJECTS_USER
                this.element = JSUserProjectList.create()
            }
            Route {
                path = "*"
                element = Typography.create { +"404 Page Not Found" }
            }
        }
    }
}