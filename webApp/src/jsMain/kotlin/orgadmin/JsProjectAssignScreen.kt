package orgadmin

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.harvest.orgProjects.FindProjectsInOrgDataModel
import com.mutualmobile.harvestKmp.features.harvest.FindUsersInOrgDataModel
import com.mutualmobile.harvestKmp.features.harvest.userProject.AssignProjectsToUsersDataModel
import csstype.*
import kotlinx.browser.window
import mui.icons.material.Add
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.router.useNavigate
import kotlin.js.Date

val JsProjectAssignScreen = VFC {
    val selectionInfo = hashMapOf<String, List<String>>()
    var userSelection by useState(hashSetOf<String>())
    var projectSelection by useState(hashSetOf<String>())

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
    val assignDataModel = AssignProjectsToUsersDataModel { stateNew: DataState ->
        isSaving = stateNew is LoadingState
        when (stateNew) {
            is SuccessState<*> -> {
                try {
                    val response =
                        (stateNew.data as ApiResponse<Unit>)
                    selectionInfo.clear()
                    userSelection.clear()
                    projectSelection.clear()
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

    val userType = UserRole.ORG_USER.role


    useEffectOnce {
        findProjectsInOrgDataModel.activate()
        usersInOrgDataModel.activate()

        findProjectsInOrgDataModel.findProjectInOrg(
            orgId = null, offset = currentProjectPage, limit = limit, search = searchProject
        )
        usersInOrgDataModel.findUsers(
            userType = userType.toInt(),
            orgIdentifier = null, isUserDeleted = false,
            currentUsersPage, limit, searchName
        )

    }


    Fragment {
        Box {
            sx {
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
                padding = 24.px
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



                    Box {
                        sx {
                            display = Display.flex
                            justifyContent = JustifyContent.spaceAround
                            justifyItems = JustifyItems.center
                        }
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

                        FormControl {
                            InputLabel {
                                +"Search Projects"
                            }
                            OutlinedInput {
                                placeholder = "Search Projects by name"
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
                                            checked = userSelection.contains(user.id)
                                            tabIndex = -1
                                            disableRipple = true
                                            onChange = { ev, checked ->
                                                val newSelection = hashSetOf<String>().apply {
                                                    addAll(userSelection)
                                                }
                                                if (checked) {
                                                    newSelection.add(user.id!!)
                                                } else {
                                                    newSelection.remove(user.id)
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
            }
            Box {
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
                                    orgId = null,
                                    offset = value.toInt().minus(1),
                                    limit = limit,
                                    search = searchProject
                                )
                            }

                        }
                        List {
                            projects?.map { project ->
                                val format: dynamic = kotlinext.js.require("date-fns").format
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
                                    ListItemIcon {
                                        Checkbox {
                                            checked = projectSelection.contains(project.id)
                                            tabIndex = -1
                                            disableRipple = true
                                            onChange = { ev, checked ->
                                                val newSelection = hashSetOf<String>().apply {
                                                    addAll(projectSelection)
                                                }
                                                if (checked) {
                                                    newSelection.add(project.id!!)
                                                } else {
                                                    newSelection.remove(project.id)
                                                }
                                                projectSelection = newSelection
                                            }
                                        }
                                    }
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
                    projectSelection.forEach { projectId ->
                        selectionInfo[projectId] = userSelection.toList()
                    }
                    assignDataModel.assignProjectsToUsers(selectionInfo)
                }
            }


            +"Save Assignments"
        }
    }
}