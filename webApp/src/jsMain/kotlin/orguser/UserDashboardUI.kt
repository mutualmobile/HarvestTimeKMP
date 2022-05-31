package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.OrgUserDashboardDataModel
import csstype.Display
import kotlinx.browser.window
import mui.system.sx
import react.VFC
import react.router.useNavigate
import react.useEffectOnce
import react.useState
import csstype.Auto.auto
import csstype.GridTemplateAreas
import csstype.array
import firebase.messaging.messaging
import firebaseApp
import mui.material.useMediaQuery
import mui.system.Box
import orguser.structure.Area
import orguser.structure.Sizes
import webKey


val UserDashboardUI = VFC {
    val mobileMode = useMediaQuery("(max-width:960px)")

    var isLoading by useState(false)
    val navigator = useNavigate()
    var isNavDrawerOpen by useState(false)

    val dataModel = OrgUserDashboardDataModel(onDataState = { stateNew ->
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

            sx {
                display = Display.grid
                gridTemplateRows = array(
                    Sizes.Header.Height,
                    auto,
                )
                gridTemplateColumns = array(
                    Sizes.Sidebar.Width, auto,
                )
                gridTemplateAreas = GridTemplateAreas(
                    arrayOf(Area.Header, Area.Header),
                    if (mobileMode)
                        arrayOf(Area.Content, Area.Content)
                    else
                        arrayOf(Area.Sidebar, Area.Content),
                )
            }

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
            if (mobileMode) OrgUserDrawer {
                open = isNavDrawerOpen
                onOpen = {
                    isNavDrawerOpen = true
                }
                onClose = {
                    isNavDrawerOpen = false
                }
            } else OrgUserSidebar()

            OrgUserContent()
        }
    }
}
