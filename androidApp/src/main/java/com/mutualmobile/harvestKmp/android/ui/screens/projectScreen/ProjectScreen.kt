package com.mutualmobile.harvestKmp.android.ui.screens.projectScreen

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.SELECTED_PROJECT
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components.ProjectListItem
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components.SearchView
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.GetUserAssignedProjectsDataModel


@Composable
fun ProjectScreen(navController: NavController) {
    val activity = LocalContext.current as Activity
    val projectListMap = remember { mutableStateOf(emptyList<OrgProjectResponse>()) }
    var filteredProjectListMap: List<OrgProjectResponse>
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    var currentUserState: DataState by remember { mutableStateOf(EmptyState) }
    var currentProjectScreenState: DataState by remember {
        mutableStateOf(EmptyState)
    }
    val getUserAssignedProjectsDataModel by remember {
        mutableStateOf(
            GetUserAssignedProjectsDataModel { projectState ->
                currentProjectScreenState = projectState
                when (projectState) {
                    is SuccessState<*> -> {
                        projectListMap.value =
                            (projectState.data as ApiResponse<*>).data as List<OrgProjectResponse>
                    }
                    else -> Unit
                }
            }
        )
    }
    val userStateModel by remember {
        mutableStateOf(
            GetUserDataModel { userState ->
                currentUserState = userState
                when (userState) {
                    is SuccessState<*> -> {
                        getUserAssignedProjectsDataModel.getUserAssignedProjects(
                            (userState.data as GetUserResponse).id ?: ""
                        )
                    }
                    else -> Unit
                }
            }.apply {
                activate()
            }
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
                    IconButton(onClick = activity::onBackPressed) {
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
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                SELECTED_PROJECT,
                                selectedProject
                            )
                            navController.popBackStack(
                                HarvestRoutes.Screen.WORK_ENTRY,
                                inclusive = false,
                                saveState = true
                            )
                        })
                }

            }
        }

    }
}


