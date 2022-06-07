package orguser

import csstype.number
import mui.icons.material.Logout
import mui.icons.material.Menu
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML


external interface HeaderProps : Props {
    var navDrawerToggle: () -> Unit
    var logout: () -> Unit
    var isLoggingOut: Boolean
}

val Header = FC<HeaderProps> { props ->

    AppBar {
        Container {
            Toolbar {
                IconButton {
                    Menu()
                    onClick = {
                        props.navDrawerToggle()
                    }

                }
                Typography {
                    sx { flexGrow = number(1.0) }
                    variant = TypographyVariant.h6
                    component = ReactHTML.div

                    +"Dashboard"
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