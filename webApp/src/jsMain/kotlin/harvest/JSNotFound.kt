package harvest

import kotlinx.css.div
import react.VFC
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1

val JSNotFound = VFC {
    div {
        h1 {
            +"404 Not found"
        }
    }
}