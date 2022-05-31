package orgadmin

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.harvest.FindProjectsInOrgDataModel
import com.mutualmobile.harvestKmp.features.harvest.FindUsersInOrgDataModel
import csstype.*
import kotlinx.browser.window
import mui.icons.material.Add
import mui.material.*
import mui.system.sx
import react.ReactNode
import react.VFC
import react.router.useNavigate
import react.useEffectOnce
import react.useState
import kotlin.js.Date

val JsProjectAssignScreen = VFC {
    val selectionInfo = useState<Map<String, List<String>>>()
    var projects by useState<List<OrgProjectResponse>>()
    var users by useState<List<FindUsersInOrgResponse>>()
    val navigator = useNavigate()
    val limit = 20
    var currentProjectPage by useState(0)
    var totalProjectPages by useState(0)

    var currentUsersPage by useState(0)
    var totalUsersPages by useState(0)

    var isLoadingProjects by useState(false)
    var isLoadingUsers by useState(false)

    val findProjectsInOrgDataModel = FindProjectsInOrgDataModel { stateNew: DataState ->
        isLoadingProjects = stateNew is LoadingState
        when (stateNew) {
            is SuccessState<*> -> {
                try {
                    val response =
                        (stateNew.data as ApiResponse<Pair<Int, List<OrgProjectResponse>>>)
                    projects = response.data?.second
                    totalProjectPages = response.data?.first ?: 0
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
    val usersInOrgDataModel = FindUsersInOrgDataModel { stateNew: DataState ->
        isLoadingUsers = stateNew is LoadingState
        when (stateNew) {
            is SuccessState<*> -> {
                try {
                    val response =
                        (stateNew.data as ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>)
                    users = response.data?.second
                    totalUsersPages = response.data?.first ?: 0
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    findProjectsInOrgDataModel.praxisCommand = { newCommand: PraxisCommand ->
        when (newCommand) {
            is NavigationPraxisCommand -> {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
            }
            is ModalPraxisCommand -> {
                window.alert(newCommand.title + "\n" + newCommand.message)
            }
        }
    }
    usersInOrgDataModel.praxisCommand = { newCommand: PraxisCommand ->
        when (newCommand) {
            is NavigationPraxisCommand -> {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
            }
            is ModalPraxisCommand -> {
                window.alert(newCommand.title + "\n" + newCommand.message)
            }
        }
    }

    val userType = UserRole.ORG_USER.role


    useEffectOnce {
        findProjectsInOrgDataModel.activate()
        usersInOrgDataModel.activate()

        findProjectsInOrgDataModel.findProjectInOrg(
            offset = currentProjectPage, limit = limit, orgId = null
        )
        usersInOrgDataModel.findUsers(
            userType = userType.toInt(),
            orgIdentifier = null, isUserDeleted = false,
            currentUsersPage, limit
        )

    }


    Box {
        sx {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            padding = 24.px
        }

        Box {
            sx {
                width = 48.pc
            }

            Box {
                sx {
                    position = Position.relative
                    transform = translatez(0.px)
                    flexGrow = number(1.0)
                    alignSelf = AlignSelf.flexEnd
                    alignItems = AlignItems.baseline
                }

                if (isLoadingUsers) {
                    CircularProgress()
                } else {
                    Pagination {
                        count = totalUsersPages
                        page = currentUsersPage
                        onChange = { event, value ->
                            currentUsersPage = value.toInt()
                            usersInOrgDataModel.findUsers(
                                userType = userType.toInt(),
                                orgIdentifier = null, isUserDeleted = false,
                                value.toInt().minus(1), limit
                            )
                        }

                    }
                    List {
                        users?.map { user ->
                            ListItem {
                                ListItemText {
                                    primary = ReactNode("${user.firstName ?: ""} ${user.lastName ?: ""}")
                                    secondary =
                                        ReactNode("${user.email}")
                                }
                            }
                        }
                    }
                }

            }
        }

        Box {
            sx {
                width = 48.pc
            }

            Box{
                if (isLoadingProjects) {
                    CircularProgress()
                } else {
                    Box {
                        sx {
                            position = Position.relative
                            transform = translatez(0.px)
                            flexGrow = number(1.0)
                            alignSelf = AlignSelf.flexEnd
                            alignItems = AlignItems.baseline
                        }
                        Pagination {
                            count = totalProjectPages
                            page = currentProjectPage
                            onChange = { event, value ->
                                currentProjectPage = value.toInt()
                                findProjectsInOrgDataModel.findProjectInOrg(
                                    offset = value.toInt().minus(1), limit = limit, orgId = null
                                )
                            }

                        }
                        List {
                            projects?.map { project ->
                                val format: dynamic = kotlinext.js.require("date-fns").format
                                val start =
                                    format(Date(project.startDate.toString()), "yyyy-MM-dd") as String
                                val end = format(Date(project.endDate.toString()), "yyyy-MM-dd") as? String
                                ListItem {
                                    ListItemText {
                                        primary =
                                            ReactNode("Name: ${project.name ?: ""}\nClient: ${project.client ?: ""}")
                                        secondary =
                                            ReactNode("Start Date: $start EndDate: $end")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}