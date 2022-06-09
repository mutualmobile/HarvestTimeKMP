package orguser.timelogging

import kotlinx.datetime.internal.JSJoda.LocalDate
import mui.icons.material.ArrowBackIos
import mui.icons.material.ArrowForwardIos
import mui.material.IconButton
import mui.material.Stack
import mui.material.StackDirection
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.responsive
import react.FC
import react.Props
import react.dom.html.ReactHTML
import kotlin.js.Date

external interface WeekSwitcherProps : Props {
    var selectedDate: LocalDate
    var onWeekChange: (MutableList<LocalDate>) -> Unit
    var currentWeek: MutableList<LocalDate>
}

val WeekSwitcher = FC<WeekSwitcherProps> { props ->
    Stack {
        this.direction = responsive(StackDirection.row)

        IconButton {
            ArrowBackIos {

            }
            onClick = {
                val week = generateWeek(props.currentWeek.first().minusWeeks(1))
                props.onWeekChange(week)
            }
        }

        IconButton {
            ArrowForwardIos {

            }
            onClick = {
                val week = generateWeek(props.currentWeek.first().plusWeeks(1))
                props.onWeekChange(week)
            }
        }

        Typography {
            variant = TypographyVariant.h6
            this.component = ReactHTML.div
            +"${format(Date(props.selectedDate.toString()), "MMMM d LLLL")}"
        }
    }
}