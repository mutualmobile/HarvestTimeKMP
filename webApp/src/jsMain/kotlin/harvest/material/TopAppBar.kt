package harvest.material

import csstype.blur
import csstype.number
import csstype.px
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.PropsWithChildren
import react.dom.html.ReactHTML
import react.fc

external interface TopAppBarProps : PropsWithChildren {
    var title: String
    var subtitle: String
}

val TopAppBar = FC<TopAppBarProps> { props ->
    Box {
        this.sx { flexGrow = number(1.0) }

        AppBar {
            this.position = AppBarPosition.static
            sx {
                backdropFilter = blur(6.px)
            }

            Toolbar {
                Stack {
                    this.direction = responsive(StackDirection.column)
                    this.sx { flexGrow = number(1.0) }
                    Typography {
                        this.variant = TypographyVariant.h6
                        this.component = ReactHTML.div
                        +props.title
                    }

                    Typography {
                        this.variant = TypographyVariant.subtitle1
                        this.component = ReactHTML.div
                        +props.subtitle
                    }
                }
                +props.children

            }
        }
    }


}