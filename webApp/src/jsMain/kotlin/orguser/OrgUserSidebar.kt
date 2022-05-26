package orguser

import csstype.Color
import csstype.None
import emotion.react.css
import mui.material.*
import react.ReactNode
import react.VFC
import react.dom.html.ReactHTML
import react.router.dom.NavLink
import react.router.useLocation
import react.useContext

val OrgUserSidebar = VFC{
    val drawerItems = useContext(OrgUserDrawerItemsContext)
    val lastPathname = useLocation().pathname.substringAfterLast("/")

    Drawer {
        anchor = DrawerAnchor.left
        variant = DrawerVariant.permanent
        Box {
            component = ReactHTML.nav
            Toolbar {}
            Divider {}
            List {
                for ((key, name) in drawerItems) {
                    NavLink {
                        to = key

                        css {
                            textDecoration = None.none
                            color = Color.currentcolor
                        }

                        ListItemButton {
                            selected = lastPathname == key

                            ListItemText {
                                primary = ReactNode(name)
                            }
                        }
                    }
                }
            }
            Divider {}
        }
    }
}