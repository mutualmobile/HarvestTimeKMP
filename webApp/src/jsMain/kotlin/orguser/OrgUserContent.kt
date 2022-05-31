package orguser

import csstype.px
import mui.material.Typography
import mui.system.Box
import mui.system.sx
import orguser.structure.Area
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
                path = "*"
                element = Typography.create { +"404 Page Not Found" }
            }
        }
    }
}