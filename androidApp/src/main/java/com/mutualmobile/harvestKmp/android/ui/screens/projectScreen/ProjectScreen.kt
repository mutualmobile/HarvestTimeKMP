package com.mutualmobile.harvestKmp.android.ui.screens.projectScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.serverDateFormatter
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components.ProjectListItem
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components.SearchView
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.GetUserAssignedProjectsDataModel
import java.util.Date
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.get


@Composable
fun ProjectScreen(
    navController: NavHostController,
    newEntryScreenViewModel: NewEntryScreenViewModel = get()
) {
    val coroutineScope = rememberCoroutineScope()
    val projectListMap = remember { mutableStateOf(emptyList<OrgProjectResponse>()) }
    var filteredProjectListMap: List<OrgProjectResponse>
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    var currentUserState: DataState by remember { mutableStateOf(EmptyState) }
    var currentProjectScreenState: DataState by remember {
        mutableStateOf(EmptyState)
    }
    var projectScreenNavigationCommands: PraxisCommand? by remember { mutableStateOf(null) }

    val getUserAssignedProjectsDataModel by remember {
        mutableStateOf(
            GetUserAssignedProjectsDataModel().apply {
                this.dataFlow.onEach { projectState ->
                    currentProjectScreenState = projectState
                    when (projectState) {
                        is SuccessState<*> -> {
                            projectListMap.value =
                                (projectState.data as ApiResponse<*>).data as List<OrgProjectResponse>
                        }
                        else -> Unit
                    } }.launchIn(coroutineScope)
                praxisCommand.onEach {  newCommand ->
                    projectScreenNavigationCommands = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            if (newCommand.screen.isBlank()) {
                                navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE
                            }
                        }
                    } }.launchIn(coroutineScope)
            }
        )
    }
    val userStateModel by remember {
        mutableStateOf(
            GetUserDataModel().apply {
                this.dataFlow.onEach { userState ->
                    currentUserState = userState
                    when (userState) {
                        is SuccessState<*> -> {
                            getUserAssignedProjectsDataModel.getUserAssignedProjects(
                                (userState.data as GetUserResponse).id ?: ""
                            )
                        }
                        else -> Unit
                    }
                }.launchIn(coroutineScope)
                praxisCommand.onEach {  newCommand ->
                    projectScreenNavigationCommands = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            if (newCommand.screen.isBlank()) {
                                navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE
                            }
                        }
                    } }.launchIn(coroutineScope)
            }.activate()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = MR.strings.choose_project.resourceId),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = { SearchView(textState) },
                contentPadding = WindowInsets.statusBars.asPaddingValues(),
            )
        },

        ) { bodyPadding ->

        Column(modifier = Modifier.padding(bodyPadding)) {
            AnimatedVisibility(visible = currentProjectScreenState is LoadingState) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                val searchedText = textState.value.text
                filteredProjectListMap = if (searchedText.isEmpty()) {
                    projectListMap.value
                } else {
                    projectListMap.value.filter { it.name?.contains(searchedText, true) == true }
                }
                items(filteredProjectListMap) { project ->
                    ProjectListItem(
                        label = project.name ?: "",
                        onItemClick = { selectedProject ->
                            newEntryScreenViewModel.updateCurrentProjectName(selectedProject)
                            project.id?.let { nnProjectId ->
                                newEntryScreenViewModel.updateCurrentWorkRequest(
                                    update = { existingRequest ->
                                        existingRequest?.copy(
                                            projectId = nnProjectId
                                        ) ?: HarvestUserWorkRequest(
                                            id = null,
                                            projectId = nnProjectId,
                                            userId = "",
                                            workDate = serverDateFormatter.format(Date()),
                                            workHours = 0f,
                                            note = null
                                        )
                                    },
                                    onUpdateCompleted = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                        })
                }

            }
        }
        HarvestDialog(praxisCommand = projectScreenNavigationCommands, onConfirm = {
            projectScreenNavigationCommands = null
        })
    }
}