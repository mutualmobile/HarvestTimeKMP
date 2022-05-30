package orguser

import csstype.Color
import csstype.None
import emotion.react.css
import mui.material.*
import react.*
import react.dom.html.ReactHTML.nav
import react.router.dom.NavLink
import react.router.useLocation


external interface OrgUserDrawerProps : Props {
    var open: Boolean
    var onOpen: () -> Unit
    var onClose: () -> Unit
}

val OrgUserDrawer = FC<OrgUserDrawerProps> { props ->
    val drawerItems = useContext(DrawerItemsContext)
    val lastPathname = useLocation().pathname.substringAfterLast("/")

    SwipeableDrawer {
        anchor = DrawerAnchor.left
        open = props.open
        onOpen = {
            props.onOpen()
        }
        onClose = {
            props.onClose()
        }
        Box {
            component = nav
            Toolbar {}
            Divider {}
            List {
                for ((key, name) in drawerItems) {
                    ListItem {
                        disablePadding = true

                        NavLink {
                            to = key

                            css {
                                textDecoration = None.none
                                color = Color.currentcolor
                            }

                            ListItemButton {
                                selected = lastPathname == key
                                ListItemIcon {
                                    mui.icons.material.Group()
                                }
                                ListItemText {
                                    primary = ReactNode(name)
                                }
                            }
                        }
                    }
                }
            }
            Divider {}
        }
    }


}