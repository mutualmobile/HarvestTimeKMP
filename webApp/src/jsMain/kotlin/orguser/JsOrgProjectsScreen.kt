package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.harvest.OrgProjectDataModel
import csstype.*
import mui.material.*
import mui.icons.material.Add
import mui.system.sx
import react.*
import react.router.useNavigate

val JsOrgProjectsScreen = VFC {
    var message by useState("")
    var createRequested by useState(false)
    val navigate = useNavigate()

    val dataModel = OrgProjectDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                val response = (stateNew.data as ApiResponse<OrgProjectResponse>)
                response.data
                message = response.message ?: "Some message"
            }
            Complete -> {
                message = "Completed loading!"
            }
            EmptyState -> {
                message = "Empty state"
            }
            is ErrorState -> {
                message = stateNew.throwable.message ?: "Error"
            }
        }
    })

    useEffectOnce {
        dataModel.activate()
    }



    Box {
        Box {
            sx {
                position = Position.relative
                transform = translatez(0.px)
                flexGrow = number(1.0)
                alignSelf = AlignSelf.flexEnd
                alignItems = AlignItems.baseline
            }
            List {
                repeat(10) {
                    ListItem {
                        ListItemText {
                            primary = ReactNode("Kotlin Multiplatform")
                            secondary =
                                ReactNode("Bluetooth and WIFI Multi Platform Module")
                        }
                    }
                }
            }

        }

        Fab {
            variant = FabVariant.extended
            sx {
                transform = translatez(0.px)
                position = Position.absolute
                bottom = 16.px
                right = 16.px
            }
            color = FabColor.primary
            Add()
            onClick = {
                createRequested = true
            }
            +"Create Project"
        }

        JsCreateProject {
            drawerOpen = createRequested
            onOpen = {
                createRequested = true
            }
            onClose = {
                createRequested = false
            }
        }
    }
}