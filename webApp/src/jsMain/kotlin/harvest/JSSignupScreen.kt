package harvest

import react.Props
import react.dom.h1
import react.fc

external interface SignupProps : Props

val JSSignupScreen = fc<SignupProps> {

    h1 {
        +"Signup Form"
    }
    h1 {
        +"Signup Status :"
    }
}