package orguser

import components.AppThemeContext
import csstype.px
import emotion.react.useTheme
import mui.material.Typography
import mui.system.Box
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.main
import react.router.Outlet
import react.router.Route
import react.router.Routes

val DEFAULT_PADDING = 30.px

val OrgUserContent = VFC {
    val drawerItems = useContext(DrawerItemsContext)
    val themeContext = useContext(AppThemeContext)
    Routes {
        Route {
            path = "/"
            element = Box.create {
                component = main
                sx {
                    padding = DEFAULT_PADDING
                }
                Box{
                    sx{
                        padding = themeContext.component1().mixins.toolbar
                    }
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