package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.harvest.ChangePasswordDataModel
import csstype.Color
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.VFC
import react.dom.html.ReactHTML
import react.dom.onChange
import react.router.dom.useSearchParams
import react.router.useNavigate
import react.useState

external interface ChangePasswordUIProps : Props {
    var drawerOpen: Boolean
    var onOpen: () -> Unit
    var onClose: () -> Unit
}

val ChangePasswordUI = FC<ChangePasswordUIProps> { props ->
    var message by useState("")
    var changePassword by useState("")
    var state by useState<DataState>()
    var password by useState("")
    val navigator = useNavigate()

    val dataModel = ChangePasswordDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = (stateNew.data as ApiResponse<*>).message ?: "Success state"
                changePassword = ""
                password = ""
                props.onClose()
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


    Drawer {
        this.variant = DrawerVariant.temporary
        this.anchor = DrawerAnchor.bottom
        open = props.drawerOpen
        onClose = { event, reason ->
            props.onClose()
        }
        sx {
            backgroundColor = Color("main")
        }
        Box {
            component = ReactHTML.nav
            TopAppBar {
                title = "Change Password Form"
                subtitle = message
            }
            Divider {}

            Paper {
                Card {
                    sx {
                        margin = Margin(24.px, 24.px)
                    }
                    Stack {
                        sx {
                            margin = Margin(24.px, 24.px)
                        }
                        TextField {
                            this.variant = FormControlVariant.outlined
                            this.value = password
                            this.onChange = {
                                val target = it.target as HTMLInputElement
                                password = target.value
                            }
                            this.placeholder = "Current Password"
                            sx {
                                margin = Margin(12.px, 2.px)
                            }
                        }

                        TextField {
                            this.variant = FormControlVariant.outlined
                            this.value = changePassword
                            this.onChange = {
                                val target = it.target as HTMLInputElement
                                changePassword = target.value
                            }
                            this.placeholder = "New Password"
                            sx {
                                margin = Margin(12.px, 2.px)
                            }
                        }

                        Button {
                            this.onClick = {
                                dataModel.changePassWord(changePassword, password)
                            }
                            +"Change Password"
                        }
                    }
                }
            }
        }
    }

}