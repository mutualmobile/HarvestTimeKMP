package orguser.timelogging

import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import react.FC
import react.Props
import react.ReactNode

external interface JSWeekTotalProps : Props {
    var workWeek: List<HarvestUserWorkResponse>?
}

val JSWeekTotal = FC<JSWeekTotalProps> { props ->

    Stack{
        direction = responsive(StackDirection.column)

        Typography{
            variant = TypographyVariant.h6
            +"${props.workWeek?.sumOf { it.workHours.toDouble() } ?: "0"} Hours"
        }
        Typography{
            variant = TypographyVariant.subtitle1
            +"Week Total"
        }
    }

}