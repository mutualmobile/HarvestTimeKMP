package workspace

import csstype.*
import harvest.material.TopAppBar
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.VFC
import react.dom.html.ReactHTML
import react.dom.onChange
import react.useState

val JsWorkspaceFindScreen = VFC {
    val status by useState("")

    TopAppBar {
        title = "Find your workspace"
        subtitle = status
    }

    Box {
        Stack {
            sx {
                margin = Margin(24.px, 24.px)
            }
            Typography {
                this.variant = TypographyVariant.h4
                this.component = ReactHTML.div
                +"Elevate your time tracking"
            }

            Typography {
                this.variant = TypographyVariant.h6
                this.component = ReactHTML.div
                +"Simple time tracking software and powerful reporting that helps your team thrive."
            }

            Box {
                this.sx {
                    this.borderRadius = 25.px
                    this.borderColor = Color("#D3D3D3")
                    this.margin = Margin(56.px, 4.px)
                }

                Typography {
                    this.variant = TypographyVariant.h6
                    this.component = ReactHTML.div
                    +"Find your organization by typing the org identifier!"
                }
                Stack {
                    this.direction = responsive(StackDirection.row)
                    this.sx {
                        this.alignItems = AlignItems.center
                        this.alignContent = AlignContent.center
                    }
                    Typography {
                        this.variant = TypographyVariant.h6
                        this.component = ReactHTML.div
                        +"http://"
                        this.sx {
                            margin = Margin(0.px, 4.px)
                        }
                    }
                    WorkspaceComp()
                    Typography {
                        this.sx {
                            margin = Margin(0.px, 4.px)
                        }
                        this.variant = TypographyVariant.h6
                        this.component = ReactHTML.div
                        +".harvestclone.com"
                    }

                }

                Button {
                    this.variant = ButtonVariant.contained
                    sx {
                        this.margin = Margin(24.px, 4.px)
                    }
                    +"Find Workspace"
                    onClick = {

                    }
                }
            }
        }
    }

}


val WorkspaceComp = VFC {
    var workspaceName by useState("")

    TextField {
        this.variant = FormControlVariant.standard
        this.value = workspaceName
        this.onChange = {
            val target = it.target as HTMLInputElement
            workspaceName = target.value
        }
        this.placeholder = "your-organization"
        sx {
            margin = Margin(12.px, 2.px)
        }
    }
}