package orguser

import mui.material.*
import react.*
import react.dom.html.ReactHTML.nav
import react.router.useLocation
import react.router.useNavigate


external interface OrgUserDrawerProps : Props {
    var open: Boolean
    var onOpen: () -> Unit
    var onClose: () -> Unit
    var drawerItems : DrawerItems?
}

val OrgUserDrawer = FC<OrgUserDrawerProps> { props ->
    val drawerItems = props.drawerItems
    val lastPathname = useLocation().pathname.substringAfterLast("/")
    val navigate = useNavigate()
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
                if (drawerItems != null) {
                    for ((key, name) in drawerItems) {
                        ListItem {
                            disablePadding = true
                            ListItemButton {
                                selected = lastPathname == key
                                ListItemIcon {
                                    mui.icons.material.Group()
                                }
                                ListItemText {
                                    primary = ReactNode(name)
                                }
                            }
                            onClick = {
                                props.onClose()
                                navigate(key)
                            }
                        }
                    }
                }
            }
            Divider {}
        }
    }


}