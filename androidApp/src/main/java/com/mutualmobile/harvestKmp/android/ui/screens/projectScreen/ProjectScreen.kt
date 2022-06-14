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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.mutualmobile.harvestKmp.android.viewmodels.ProjectScreenViewModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import java.util.Date
import org.koin.androidx.compose.get


@Composable
fun ProjectScreen(
    navController: NavHostController,
    nesVm: NewEntryScreenViewModel = get(),
    userState: DataState,
    psVm: ProjectScreenViewModel = get(),
) {
    LaunchedEffect(psVm.projectScreenNavigationCommands) {
        when (psVm.projectScreenNavigationCommands) {
            is NavigationPraxisCommand -> {
                if ((psVm.projectScreenNavigationCommands as NavigationPraxisCommand).screen.isBlank()) {
                    navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE
                }
            }
        }
    }

    LaunchedEffect(userState) {
        when (userState) {
            is SuccessState<*> -> { psVm.getUserAssignedProjects(userState = userState) }
            else -> Unit
        }
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
                actions = {
                    SearchView(psVm.textState) { updatedState ->
                        psVm.textState = updatedState
                    }
                },
                contentPadding = WindowInsets.statusBars.asPaddingValues(),
            )
        },

        ) { bodyPadding ->

        Column(modifier = Modifier.padding(bodyPadding)) {
            AnimatedVisibility(visible = psVm.currentProjectScreenState is LoadingState) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                val searchedText = psVm.textState.text
                psVm.filteredProjectListMap = if (searchedText.isEmpty()) {
                    psVm.projectListMap
                } else {
                    psVm.projectListMap.filter { it.name?.contains(searchedText, true) == true }
                }
                items(psVm.filteredProjectListMap) { project ->
                    ProjectListItem(
                        label = project.name ?: "",
                        onItemClick = { selectedProject ->
                            nesVm.updateCurrentProjectName(selectedProject)
                            project.id?.let { nnProjectId ->
                                nesVm.updateCurrentWorkRequest(
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
                                        psVm.resetAll {
                                            navController.navigateUp()
                                        }
                                    }
                                )
                            }
                        })
                }

            }
        }
        HarvestDialog(praxisCommand = psVm.projectScreenNavigationCommands, onConfirm = {
            psVm.projectScreenNavigationCommands = null
        })
    }
}