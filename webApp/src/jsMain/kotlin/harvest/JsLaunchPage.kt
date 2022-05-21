package harvest

import mui.material.Button
import mui.material.Stack
import react.VFC
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.h1
import react.router.dom.NavLink
import react.router.getToPathname
import react.router.useLocation
import react.router.useNavigate

val JSHomePage  = VFC {

    val navigator = useNavigate()

    h1 {
        +"Welcome to harvest clone!"
    }

    Stack{
        Button{
            onClick = {
                navigator.invoke(to = "/login")
            }
            +"Login"
        }

        Button{
            onClick = {
                navigator.invoke(to = "/signup")
            }
            +"Signup"
        }

        Button{
            onClick = {
                navigator.invoke(to = "/trendingui")
            }
            +"Trending UI"
        }
    }


}