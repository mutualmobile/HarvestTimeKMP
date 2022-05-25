package orguser

import harvest.material.TopAppBar
import mui.material.Box
import react.VFC
import react.router.useNavigate
import react.useState

val UserDashboardUI = VFC {
    var message by useState("")
    val navigator = useNavigate()


    Box {
        TopAppBar {
            title = "Organization User"
            subtitle = ""
        }
    }

}