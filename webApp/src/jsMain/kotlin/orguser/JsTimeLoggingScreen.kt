package orguser

import csstype.Display
import csstype.FlexDirection
import csstype.px
import harvest.material.TopAppBar
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.router.useNavigate
import react.useEffectOnce
import react.useState
import kotlin.js.Date

val JsTimeLoggingScreen = FC<Props> {
    var message by useState("")
    var today by useState(Date().getDate())
    var selectedDate by useState(Date().getDate())
    val navigate = useNavigate()
    val month = Date().getMonth() + 1
    val week = mutableListOf<Int>()
    val days = mutableListOf("Mon", "Tues", "Wed", "Thu", "Fri", "Sat", "Sun")

    fun generateWeek() {
        val date = Date()
        today = date.getDate()
        for (i in 1..7) {
            val first = date.getDate() - date.getDay() + i
            week.add(first)
        }
    }

    useEffectOnce {
        generateWeek()
    }

    Box {
        TopAppBar {
            title = "Time logging for ${Date()}"
            subtitle = message
        }


        mui.material.List {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.row
                padding = 0.px
            }
            week.mapIndexed { index, date ->
                DayOfWeekView {
                    weekDate = date
                    isToday = date == today
                    isSelected = date == selectedDate
                    weekTitle = days[index]
                }
            }
        }
    }
}

external interface DayOfWeekViewProps : Props {
    var weekDate: Int
    var isToday: Boolean
    var isSelected: Boolean
    var weekTitle: String
}


val DayOfWeekView = FC<DayOfWeekViewProps> {

}