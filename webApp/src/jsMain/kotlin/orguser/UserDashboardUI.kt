package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.LogoutDataModel
import kotlinx.browser.window
import react.VFC
import react.router.useNavigate
import react.useEffectOnce
import react.useState
import firebase.messaging.messaging
import firebaseApp
import mui.system.Box
import webKey


val UserDashboardUI = VFC {

    var isLoading by useState(false)
    val navigator = useNavigate()
    var isNavDrawerOpen by useState(false)

    val dataModel = LogoutDataModel(onDataState = { stateNew ->
        isLoading = stateNew is LoadingState

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



    DrawerItemsModule {
        Box {

            Header {
                this.isLoggingOut = isLoading
                this.logout = {

                    firebaseApp?.messaging()?.getToken(webKey)?.then {
                        firebaseApp?.messaging()?.deleteToken(it)?.then {
                            dataModel.logout()
                        }
                    }

                }
                this.navDrawerToggle = {
                    isNavDrawerOpen = !isNavDrawerOpen
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

            OrgUserContent()
        }
    }
}
