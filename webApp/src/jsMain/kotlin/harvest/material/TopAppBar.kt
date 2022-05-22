package harvest.material

import csstype.number
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.Props
import react.dom.html.ReactHTML
import react.fc

external interface TopAppBarProps : Props {
    var title: String
    var subtitle: String
}

val TopAppBar = fc<TopAppBarProps> { props ->
    Box {
        this.attrs.sx { flexGrow = number(1.0) }

        AppBar {
            this.attrs.position = AppBarPosition.static

            Toolbar {
                Stack {
                    this.attrs.direction = responsive(StackDirection.column)
                    Typography {
                        this.attrs.sx { flexGrow = number(1.0) }
                        this.attrs.variant = TypographyVariant.h6
                        this.attrs.component = ReactHTML.div
                        +props.title
                    }

                    Typography {
                        this.attrs.sx { flexGrow = number(1.0) }
                        this.attrs.variant = TypographyVariant.subtitle1
                        this.attrs.component = ReactHTML.div
                        +props.subtitle
                    }
                }

            }
        }
    }


}