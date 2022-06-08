package orguser.timelogging

import csstype.px
import mui.material.Stack
import mui.material.StackDirection
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props

external interface NewTimeEntryButtonProps : Props {
    var onClicked: () -> Unit
}

val NewTimeEntryButton = FC<NewTimeEntryButtonProps> { props ->
    Stack {
        direction = responsive(StackDirection.column)
        NewEntryButton {
            clicked = {
                props.onClicked()
            }
        }
        Typography {
            sx {
                marginTop = 4.px
            }
            variant = TypographyVariant.subtitle2
            +"New Entry"
        }
    }
}