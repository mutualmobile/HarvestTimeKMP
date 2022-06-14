package orguser

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.listProjectsAssignedToUser
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgUsersApiDataModels.FindUsersInOrgDataModel
import csstype.*
import kotlinext.js.asJsObject
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mui.icons.material.Add
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.html.TdAlign
import react.dom.onChange
import react.router.useNavigate

val JsOrgUsersScreen = VFC {
    var totalPages by useState(0)
    val navigator = useNavigate()
    var userType by useState(UserRole.ORG_ADMIN.role)
    var users by useState<List<FindUsersInOrgResponse>>()
    var currentPage by useState(0)
    var limit by useState(10)
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
                }
                else -> {}
            }
        }.launchIn(this.dataModelScope)
    }

    dataModel.praxisCommand.onEach { newCommand ->
        when (newCommand) {
            is NavigationPraxisCommand -> {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
            }
            is ModalPraxisCommand -> {
                window.alert(newCommand.title + "\n" + newCommand.message)
            }
        }
    }.launchIn(dataModel.dataModelScope)

    useEffect(currentPage, limit, searchName) {
        dataModel.findUsers(
            userType = userType.toInt(),
            orgIdentifier = null, isUserDeleted = false,
            currentPage, limit, searchName
        )
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

        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.row
            }

            FormControl {
                sx {
                    margin = 4.px
                }
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
                sx {
                    margin = 4.px
                }
                InputLabel {
                    +"Search..."
                }
                OutlinedInput {
                    placeholder = "Search by name"
                    onChange = {
                        val target = it.target as HTMLInputElement
                        searchName = target.value
                    }
                }
            }
        }




        if (isLoading) {
            CircularProgress()
        } else {
            TableContainer {
                component = Paper
                Table {
                    stickyHeader = true
                    sx {
                        minWidth = 450.px
                    }
                    TableHead {
                        TableRow {
                            TableCell {
                                align = TdAlign.left
                                +"First Name"
                            }
                            TableCell {
                                align = TdAlign.left
                                +"Last Name"
                            }
                            TableCell {
                                align = TdAlign.left
                                +"Email"
                            }
                        }
                    }
                    TableBody {
                        users?.map { user ->
                            TableRow {
                                onClick = {
                                    navigator.invoke(
                                        BROWSER_SCREEN_ROUTE_SEPARATOR + HarvestRoutes.Screen.LIST_PROJECTS_USER.listProjectsAssignedToUser(
                                            userId = user.id
                                        )
                                    )
                                }
                                key = user.id
                                TableCell {
                                    align = TdAlign.left
                                    +"${user.firstName}"
                                }
                                TableCell {
                                    align = TdAlign.left
                                    +"${user.lastName}"
                                }
                                TableCell {
                                    align = TdAlign.left
                                    +"${user.email}"
                                }
                            }
                        }
                    }
                }

            }
            TablePagination {
                count = -1 //  the next and previous buttons were disabled regardless of count = totalPages being specified
                page = currentPage
                rowsPerPage = limit
                onRowsPerPageChange = { event ->
                    val value =
                        Json.decodeFromString<ValueTarget>(JSON.stringify(event.target).replace("\\","")) // dirty fix event.target.value not accessible
                    limit =(value.value)
                    currentPage = (0)
                }
                onPageChange = { _, value ->
                    currentPage = (value.toInt())
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

@Serializable
data class ValueTarget(var value: Int)