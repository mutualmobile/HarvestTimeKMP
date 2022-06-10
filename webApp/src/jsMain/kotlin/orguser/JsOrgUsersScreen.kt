package orguser

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.listProjectsAssignedToUser
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgUsersApiDataModels.FindUsersInOrgDataModel
import csstype.*
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mui.icons.material.Add
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.router.useNavigate

val JsOrgUsersScreen = VFC {
    var totalPages by useState(0)
    val navigator = useNavigate()
    var userType by useState(UserRole.ORG_ADMIN.role)
    var users by useState<List<FindUsersInOrgResponse>>()
    var currentPage by useState(0)
    val limit = 10
    var isLoading by useState(false)
    var searchName by useState<String>()

    val dataModel = FindUsersInOrgDataModel().apply {
        this.dataFlow.onEach { stateNew ->
            isLoading = stateNew is PraxisDataModel.LoadingState
            when (stateNew) {
                is PraxisDataModel.SuccessState<*> -> {
                    try {
                        val response =
                            (stateNew.data as ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>)
                        users = response.data?.second
                        totalPages = response.data?.first ?: 0
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }else -> {}
            }
        }.launchIn(this.dataModelScope)
    }

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
        dataModel.findUsers(
            userType = userType.toInt(),
            orgIdentifier = null, isUserDeleted = false,
            0, limit, searchName
        )
    }

    Box {
        sx {
            position = Position.relative
            transform = translatez(0.px)
            flexGrow = number(1.0)
            alignSelf = AlignSelf.flexEnd
            alignItems = AlignItems.baseline
        }

        FormControl {
            InputLabel {
                +"User Type"
            }

            Select {
                value = userType.unsafeCast<Nothing?>()
                label = ReactNode("UserType")
                onChange = { event, _ ->
                    dataModel.findUsers(
                        userType = event.target.value.toInt(),
                        orgIdentifier = null, isUserDeleted = false,
                        0, limit, searchName
                    )
                    userType = event.target.value
                    currentPage = 0
                }
                MenuItem {
                    value = UserRole.ORG_ADMIN.role
                    +"Org Admins"
                }
                MenuItem {
                    value = UserRole.ORG_USER.role
                    +"Users"
                }


            }
        }

        FormControl {
            InputLabel {
                +"Search"
            }
            OutlinedInput {
                placeholder = "Search by name"
                onChange = {
                    val target = it.target as HTMLInputElement
                    searchName = target.value
                    dataModel.findUsers(
                        userType = userType.toInt(),
                        orgIdentifier = null, isUserDeleted = false,
                        0, limit, target.value
                    )
                    currentPage = 0
                }
            }
        }


        if (isLoading) {
            CircularProgress()
        } else {
            Pagination {
                count = totalPages
                page = currentPage
                onChange = { event, value ->
                    currentPage = value.toInt()
                    dataModel.findUsers(
                        userType = userType.toInt(),
                        orgIdentifier = null, isUserDeleted = false,
                        value.toInt().minus(1), limit, searchName
                    )
                }

            }
            List {
                users?.map { user ->
                    ListItem {
                        ListItemText {
                            primary = ReactNode("${user.firstName ?: ""} ${user.lastName ?: ""}")
                            secondary = ReactNode("${user.email}")
                        }
                        IconButton {
                            mui.icons.material.ArrowForwardIos()
                            onClick = {
                                navigator.invoke(
                                    BROWSER_SCREEN_ROUTE_SEPARATOR + HarvestRoutes.Screen.LIST_PROJECTS_USER.listProjectsAssignedToUser(
                                        userId = user.id
                                    )
                                )
                            }
                        }
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

        }
        +"Create User"
    }
}