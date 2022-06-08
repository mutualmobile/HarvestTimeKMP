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

external interface OrgUserContentProps : Props {
    var drawerItems: DrawerItems?
}

val OffsetAppBar = VFC {
    val themeContext by useContext(AppThemeContext)
    Box {
        sx {
            padding = themeContext.mixins.toolbar
        }
    }
}


val OrgUserContent = FC<OrgUserContentProps> { props ->
    Routes {
        Route {
            path = "/"
            element = Box.create {
                component = main
                sx {
                    padding = DEFAULT_PADDING
                }
                OffsetAppBar()
                Outlet()
            }

            props.drawerItems?.forEachIndexed { i, (key, _, Component) ->
                if (i == 0) {
                    Route {
                        index = true
                        path = null
                        element = Component.create()
                    }
                    Route {
                        path = key
                        element = Component.create()
                    }
                } else {
                    Route {
                        path = key
                        element = Component.create()
                    }
                }

            }
            Route {
                path = "*"
                element = Typography.create { +"404 Page Not Found" }
            }
        }
    }
}