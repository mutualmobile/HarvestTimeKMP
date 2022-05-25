package orguser

import csstype.Color
import csstype.None
import emotion.react.css
import kotlinx.js.jso
import mui.material.*
import mui.system.sx
import react.ReactNode
import react.VFC
import react.key
import react.useState
import react.dom.html.ReactHTML.nav
import react.router.dom.NavLink
import react.router.useLocation

val OrgUserDrawer = VFC {
    var drawerOpen by useState(false)
    val lastPathname = useLocation().pathname.substringAfterLast("/")

    fun toggleDrawer() {
        drawerOpen = !drawerOpen
    }

    Drawer{
        variant = DrawerVariant.permanent
        anchor = DrawerAnchor.left
        Box {
            component = nav
            Toolbar {}
            Divider {}
            List {
                arrayOf("projects", "users", "settings").map { text ->
                    ListItem {
                        key = text
                        disablePadding = true

                        NavLink {
                            to = text

                            css {
                                textDecoration = None.none
                                color = Color.currentcolor
                            }

                            ListItemButton {
                                selected = lastPathname == text

                                ListItemText {
                                    primary = ReactNode(text)
                                }
                            }
                        }
                    }
                }
            }
            Divider{}
        }
    }


}