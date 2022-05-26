package orguser.structure

import csstype.ident
import csstype.px

object Area {
    val Header = ident("header")
    val Sidebar = ident("sidebar")
    val Content = ident("content")
}


object Sizes {
    object Header {
        val Height = 64.px
    }

    object Sidebar {
        val Width = 135.px
    }
}