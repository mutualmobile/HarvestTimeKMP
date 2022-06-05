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

external interface OrgUserContentProps :Props{
    var drawerItems:DrawerItems?
}


val OrgUserContent = FC<OrgUserContentProps> { props->
    val themeContext by useContext(AppThemeContext)
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
                        padding = themeContext.mixins.toolbar
                    }
                }
                Outlet()
            }

            props.drawerItems?.forEachIndexed { i, (key, _, Component) ->
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