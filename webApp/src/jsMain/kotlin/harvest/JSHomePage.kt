package harvest

import react.Props
import react.dom.html.ReactHTML.h1
import react.fc
import react.router.dom.Link

external interface HomeProps : Props

val JSHomePage  = fc<HomeProps> {

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