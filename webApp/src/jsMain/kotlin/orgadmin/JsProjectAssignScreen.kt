package orgadmin

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels.FindProjectsInOrgDataModel
import com.mutualmobile.harvestKmp.features.datamodels.orgUsersApiDataModels.FindUsersInOrgDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.AssignProjectsToUsersDataModel
import csstype.*
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mui.icons.material.Add
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.router.useNavigate
import kotlin.js.Date

val JsProjectAssignScreen = VFC {
    val selectionInfo = hashMapOf<String, List<String>>()
    var userSelection by useState<HashSet<FindUsersInOrgResponse>>(hashSetOf())
    var projectSelection by useState<OrgProjectResponse>()

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
    var isSaving by useState(false)
    var searchName by useState<String>()
    var searchProject by useState<String>()
    val userType = UserRole.ORG_USER.role

    val findProjectsInOrgDataModel = FindProjectsInOrgDataModel().apply {
        this.dataFlow.onEach { stateNew: PraxisDataModel.DataState ->
            isLoadingProjects = stateNew is PraxisDataModel.LoadingState
            when (stateNew) {
                is PraxisDataModel.SuccessState<*> -> {
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
        }.launchIn(dataModelScope)
    }
    val usersInOrgDataModel = FindUsersInOrgDataModel().apply {
        this.dataFlow.onEach { stateNew: PraxisDataModel.DataState ->
            isLoadingUsers = stateNew is PraxisDataModel.LoadingState
            when (stateNew) {
                is PraxisDataModel.SuccessState<*> -> {
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
        }.launchIn(dataModelScope)
    }
    val assignDataModel = AssignProjectsToUsersDataModel().apply {
        this.dataFlow.onEach { stateNew: PraxisDataModel.DataState ->
            isSaving = stateNew is PraxisDataModel.LoadingState
            when (stateNew) {
                is PraxisDataModel.SuccessState<*> -> {
                    try {
                        selectionInfo.clear()
                        userSelection.clear()
                        projectSelection = null
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }.launchIn(dataModelScope)
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
    assignDataModel.praxisCommand = { newCommand: PraxisCommand ->
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
        findProjectsInOrgDataModel.activate()
        usersInOrgDataModel.activate()
        assignDataModel.activate()
        findProjectsInOrgDataModel.findProjectInOrg(
            orgId = null, offset = currentProjectPage, limit = limit, search = searchProject
        )
        usersInOrgDataModel.findUsers(
            userType = userType.toInt(),
            orgIdentifier = null, isUserDeleted = false,
            currentUsersPage, limit, searchName
        )

    }


    Box {
        projectSelection?.let {
            Typography {
                variant = TypographyVariant.h6
                +"Select users for assignment!"
            }

            Box {
                sx {
                    flexDirection = FlexDirection.row
                }
                Box {
                    sx {
                        flexDirection = FlexDirection.column
                    }

                    Typography {
                        variant = TypographyVariant.h4
                        +"${it.name}"
                    }
                    Typography {
                        variant = TypographyVariant.h5
                        +"${it.client}"
                    }

                }


                IconButton {
                    mui.icons.material.CloseRounded()
                    onClick = {
                        projectSelection = null
                    }
                }
            }

            Box {
                FormControl {
                    InputLabel {
                        +"Search Users"
                    }
                    OutlinedInput {
                        placeholder = "Search Users by name"
                        onChange = {
                            val target = it.target as HTMLInputElement
                            usersInOrgDataModel.findUsers(
                                userType = userType.toInt(),
                                orgIdentifier = null, isUserDeleted = false,
                                0, limit, target.value
                            )
                            currentUsersPage = 0
                            searchName = target.value
                        }
                    }
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
                                value.toInt().minus(1), limit, searchName
                            )
                        }

                    }
                    List {
                        users?.map { user ->
                            ListItem {
                                ListItemIcon {
                                    Checkbox {
                                        checked = userSelection.contains(user)
                                        tabIndex = -1
                                        disableRipple = true
                                        onChange = { ev, checked ->
                                            val newSelection =
                                                hashSetOf<FindUsersInOrgResponse>().apply {
                                                    addAll(userSelection)
                                                }
                                            if (checked) {
                                                newSelection.add(user)
                                            } else {
                                                newSelection.remove(user)
                                            }
                                            userSelection = newSelection
                                        }
                                    }
                                }
                                ListItemText {
                                    primary =
                                        ReactNode("${user.firstName ?: ""} ${user.lastName ?: ""}")
                                    secondary =
                                        ReactNode("${user.email}")
                                }
                            }
                        }
                    }
                }
            }
        } ?: run {
            // project form control for search and list with pagination.
            Container {
                sx {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                }

                Typography {
                    variant = TypographyVariant.h6
                    +"Select a Project to assign users"
                }

                FormControl {
                    InputLabel {
                        +"Search Project"
                    }
                    OutlinedInput {
                        placeholder = "Search Project by name"
                        onChange = {
                            val target = it.target as HTMLInputElement
                            findProjectsInOrgDataModel.findProjectInOrg(
                                offset = currentProjectPage,
                                limit = limit,
                                orgId = null,
                                search = target.value
                            )
                            currentUsersPage = 0
                            searchProject = target.value
                        }
                    }
                }


                if (isLoadingProjects) {
                    CircularProgress()
                } else {
                    Box {
                        Pagination {
                            count = totalProjectPages
                            page = currentProjectPage
                            onChange = { event, value ->
                                currentProjectPage = value.toInt()
                                findProjectsInOrgDataModel.findProjectInOrg(
                                    orgId = null,
                                    offset = value.toInt().minus(1),
                                    limit = limit,
                                    search = searchProject
                                )
                            }

                        }
                        List {
                            projects?.map { project ->
                                val format: dynamic =
                                    kotlinext.js.require("date-fns").format
                                val start =
                                    format(
                                        Date(project.startDate.toString()),
                                        "yyyy-MM-dd"
                                    ) as String
                                val end = format(
                                    Date(project.endDate.toString()),
                                    "yyyy-MM-dd"
                                ) as? String
                                ListItem {
                                    ListItemText {
                                        primary =
                                            ReactNode("Name: ${project.name ?: ""}\nClient: ${project.client ?: ""}")
                                        secondary =
                                            ReactNode("Start Date: $start EndDate: $end")
                                    }
                                    onClick = {
                                        projectSelection = project
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }


    }

    Fab {
        variant = FabVariant.extended
        color = FabColor.primary
        sx {
            transform = translatez(0.px)
            position = Position.absolute
            bottom = 16.px
            right = 16.px
        }
        color = FabColor.primary
        if (isSaving) {
            CircularProgress {
                color = CircularProgressColor.secondary
            }
        } else {
            Add()
            onClick = {
                projectSelection?.let {
                    userSelection.takeIf { it.isNotEmpty() }?.filter { it.id != null }
                        ?.map { it.id!! }?.let {
                            selectionInfo[projectSelection?.id!!] = it
                            assignDataModel.assignProjectsToUsers(selectionInfo)
                        }
                }


            }
        }


        +"Save Assignments"
    }
}