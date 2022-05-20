package harvest

import react.Props
import react.VFC
import react.dom.html.ReactHTML.h1
import react.fc
import react.router.dom.Link

val JSHomePage  = VFC {

    h1 {
        +"Welcome to harvest clone!"
    }

    Link {
        to("/login")
        +"Login"
    }

    Link {
        to("/signup")
        +"Signup"
    }
}