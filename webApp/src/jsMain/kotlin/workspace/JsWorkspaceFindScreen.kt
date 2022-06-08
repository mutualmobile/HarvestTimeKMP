package workspace

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgApiDataModels.FindOrgByIdentifierDataModel
import csstype.*
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.html.ReactHTML
import react.dom.onChange
import react.router.useNavigate

val JsWorkspaceFindScreen = VFC {
    var status by useState("")
    var workspaceName by useState("")
    val navigator = useNavigate()
    val dataModel = FindOrgByIdentifierDataModel(onDataState = { dataState: DataState ->
        when (dataState) {
            is LoadingState -> {
                status = "Loading..."
            }
            is SuccessState<*> -> {
                val organization = (dataState.data as ApiResponse<HarvestOrganization>).data
                status = "Found organization! ${organization?.name}"
            }
            is ErrorState -> {
                status = dataState.throwable.message.toString()
            }
        }
    })


    dataModel.praxisCommand = { newCommand ->
        when (newCommand) {
            is NavigationPraxisCommand -> {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
            }
            is ModalPraxisCommand -> {
                window.alert(newCommand.title + "\n" + newCommand.message)
            }
        }
    }


    useEffectOnce {
        dataModel.activate()
    }



    TopAppBar {
        title = "Find your workspace"
        subtitle = status
    }

    Card{
        sx {
            padding = 12.px
            margin = 12.px
            borderRadius = 4.px
            minWidth = 100.px
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
                +"https://"
                this.sx {
                    margin = Margin(0.px, 4.px)
                }
            }
            WorkspaceComp {
                this.name = workspaceName
                this.nameUpdate = {
                    workspaceName = it.lowercase().trim()
                }
            }
            Typography {
                this.sx {
                    margin = Margin(0.px, 4.px)
                }
                this.variant = TypographyVariant.h6
                this.component = ReactHTML.div
                +".harvestkmp.com"
            }

        }

        Button {
            this.variant = ButtonVariant.contained
            sx {
                this.margin = Margin(24.px, 4.px)
            }
            +"Find Workspace"
            onClick = {
                dataModel.findOrgByIdentifier(workspaceName)
            }
        }

        Button {
            sx {
                this.margin = Margin(24.px, 4.px)
            }
            +"Signup new organization ?"
            onClick = {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + HarvestRoutes.Screen.SIGNUP)
            }
        }

    }
}


external interface WorkspaceProps : Props {
    var name: String
    var nameUpdate: (String) -> Unit
}

val WorkspaceComp = FC<WorkspaceProps> { props ->
    TextField {
        this.variant = FormControlVariant.standard
        this.value = props.name
        this.onChange = {
            val target = it.target as HTMLInputElement
            props.nameUpdate(target.value)
        }
        this.placeholder = "your-organization"
        sx {
            margin = Margin(12.px, 2.px)
        }
    }
}