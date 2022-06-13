package orguser

import components.AppThemeContext
import csstype.Display
import csstype.number
import kotlinx.js.jso
import mui.icons.material.Logout
import mui.icons.material.Menu
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.Breakpoint
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML
import react.key
import react.router.useNavigate
import react.useContext


external interface HeaderProps : Props {
    var navDrawerToggle: () -> Unit
    var logout: () -> Unit
    var isLoggingOut: Boolean
    var showOptionsInHeader: Boolean
    var drawerItems: DrawerItems?
}

val Header = FC<HeaderProps> { props ->
    val drawerItems = props.drawerItems
    val navigate = useNavigate()

    AppBar {
        position = AppBarPosition.static
        Container {
            maxWidth = "xl"
            Toolbar {
                disableGutters = true
                if (!props.showOptionsInHeader) {
                    IconButton {
                        Menu()
                        onClick = {
                            props.navDrawerToggle()
                        }
                    }
                }

                Stack {
                    sx { flexGrow = number(1.0) }
                    spacing = responsive(2)
                    direction = responsive(StackDirection.row)
                    Typography {
                        variant = TypographyVariant.h5
                        component = ReactHTML.div
                        +"Dashboard"
                    }
                    if (props.showOptionsInHeader) {
                        drawerItems?.forEach { item ->
                            MenuItem {
                                key = item.key
                                onClick = {
                                    navigate(item.key)
                                }
                                Typography {
                                    align = TypographyAlign.center
                                    +item.name
                                }
                            }
                        }

                    }
                }

                if (props.isLoggingOut) {
                    CircularProgress()
                } else {
                    IconButton {
                        Logout()
                        onClick = {
                            props.logout()
                        }
                    }
                }

            }
        }
    }
}