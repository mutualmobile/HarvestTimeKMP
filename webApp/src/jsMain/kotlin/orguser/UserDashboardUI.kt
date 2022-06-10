package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.UserDashboardDataModel
import components.AppThemeContext
import csstype.NamedColor
import kotlinx.browser.window
import react.VFC
import react.router.useNavigate
import react.useEffectOnce
import react.useState
import firebase.messaging.messaging
import firebaseApp
import mui.material.Backdrop
import mui.material.CircularProgress
import mui.material.styles.useTheme
import mui.material.useMediaQuery
import mui.system.Box
import mui.system.Breakpoint
import mui.system.sx
import react.useContext
import webKey


val UserDashboardUI = VFC {

    var isLoggingOut by useState(false)
    var isLoadingUser by useState(false)
    val navigator = useNavigate()
    var isNavDrawerOpen by useState(false)
    var drawer by useState<DrawerItems>()
    val themeContext by useContext(AppThemeContext)
    val matchesSM = useMediaQuery(themeContext.breakpoints.up(Breakpoint.sm))
    val matchesMD = useMediaQuery(themeContext.breakpoints.up(Breakpoint.md))
    val matchesXS = useMediaQuery(themeContext.breakpoints.up(Breakpoint.xs))
    val matchesLG = useMediaQuery(themeContext.breakpoints.up(Breakpoint.lg))
    val matchesXL = useMediaQuery(themeContext.breakpoints.up(Breakpoint.xl))

    val dataModel = UserDashboardDataModel(onDataState = { stateNew ->
        isLoggingOut = stateNew is LogoutInProgress
        isLoadingUser = stateNew is LoadingState
        if (stateNew is SuccessState<*>) {
            val data = stateNew.data
            if (data is GetUserResponse) {
                drawer = data.role?.let { drawerItems(it) }
                println("data role is ${data.role}")
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


    Box {
        Header {
            this.showOptionsInHeader = matchesLG || matchesXL
            this.isLoggingOut = isLoggingOut
            this.drawerItems = drawer
            this.logout = {
                dataModel.logout()
                firebaseApp?.messaging()?.getToken(webKey)?.then {
                    firebaseApp?.messaging()?.deleteToken(it)?.then {
                    }
                }
            }
            this.navDrawerToggle = {
                isNavDrawerOpen = !isNavDrawerOpen
            }
        }

        if (!isLoadingUser && drawer != null && (matchesSM || matchesXS || matchesMD)) {
            OrgUserDrawer {
                open = isNavDrawerOpen
                drawerItems = drawer
                onOpen = {
                    isNavDrawerOpen = true
                }
                onClose = {
                    isNavDrawerOpen = false
                }
            }

            OrgUserContent {
                drawerItems = drawer
            }
        } else {
            Backdrop {
                sx {
                    color = NamedColor.lightgray
                }
                open = isLoadingUser
                CircularProgress {

                }
            }
        }


    }

}
