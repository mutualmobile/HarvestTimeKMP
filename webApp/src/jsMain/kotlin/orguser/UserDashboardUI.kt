package orguser

import react.VFC
import react.router.useNavigate
import react.useState

val UserDashboardUI = VFC {
    var message by useState("")
    val navigator = useNavigate()

}