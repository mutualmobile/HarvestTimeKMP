package orguser

import harvest.material.TopAppBar
import mui.material.Box
import react.FC
import react.Props
import react.router.useNavigate
import react.useState
import kotlin.js.Date

val JsTimeLoggingScreen = FC<Props> {
    var message by useState("")
    val selectedDate by useState(Date())
    val navigate = useNavigate()


    Box {
        TopAppBar {
            title = "Time logging for ${selectedDate.toDateString()}"
            subtitle = message
        }
    }
}