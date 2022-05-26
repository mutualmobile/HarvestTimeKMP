package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.harvest.OrgUserDashboardDataModel
import csstype.number
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.icons.material.Menu
import mui.icons.material.Logout

import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.VFC
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML.div
import react.router.useNavigate
import react.useEffectOnce
import react.useState

val UserDashboardUI = VFC {
    var message by useState("")
    val navigator = useNavigate()
    var isNavDrawerOpen by useState(false)

    val dataModel = OrgUserDashboardDataModel(onDataState = {stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = (stateNew.data as LoginResponse).message ?: "Some message"
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

    useEffectOnce {
        dataModel.activate()
    }



    OrgUserDrawerItemsModule {
        Box {
            AppBar {
                Container {
                    Toolbar {
                        IconButton {
                            ariaLabel = "menu"
                            ariaHasPopup = AriaHasPopup.`false`
                            onClick = {
                                isNavDrawerOpen = !isNavDrawerOpen
                            }
                            Menu()
                        }
                        Typography {
                            sx { flexGrow = number(1.0) }
                            variant = TypographyVariant.h6
                            component = div

                            +"Organization User"
                        }
                        IconButton {
                            ariaLabel = "logout"
                            ariaHasPopup = AriaHasPopup.`false`
                            onClick = {
                                dataModel.logout()
                            }
                            Logout()
                        }
                    }
                }
            }
            OrgUserDrawer {
                open = isNavDrawerOpen
                onOpen = {
                    isNavDrawerOpen = true
                }
                onClose = {
                    isNavDrawerOpen = false
                }
            }
        }
    }
}