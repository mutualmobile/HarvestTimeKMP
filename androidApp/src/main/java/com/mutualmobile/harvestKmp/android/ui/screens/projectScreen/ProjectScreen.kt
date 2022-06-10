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
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components.ProjectListHeader
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components.ProjectListItem
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components.SearchView
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.GetUserAssignedProjectsDataModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectScreen(navController: NavController) {
    val activity = LocalContext.current as Activity
    var projectListMap =
        mapOf("P" to listOf("Praxis", "PraxisFlutter"), "M" to listOf("MagicMountain", "Treat"))
    var filteredProjectListMap: Map<String, List<String>>
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    var currentProjectScreenState: DataState by remember {
        mutableStateOf(EmptyState)
    }
    val getUserAssignedProjectsDataModel by remember {
        mutableStateOf(
            GetUserAssignedProjectsDataModel { projectState ->
                currentProjectScreenState = projectState
                when (projectState) {
                    is SuccessState<*> -> {
                        val apiResponse = (projectState.data as ApiResponse<*>)
                        projectListMap = mapOf(
                            "P" to listOf("Praxis", "PraxisFlutter"),
                            "M" to listOf("MagicMountain", "Treat")
                        )
                    }
                    else -> Unit
                }
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
                    projectListMap
                } else {
                    var tempProjectListMap = emptyMap<String, List<String>>()
                    projectListMap.filter { (key, value) ->
                        val list = value.filter { it.contains(searchedText, true) }
                        if (list.isEmpty().not()) {
                            tempProjectListMap = tempProjectListMap + Pair(key, list)
                        }

                        tempProjectListMap.isEmpty()


                    }
                    tempProjectListMap
                }

                filteredProjectListMap.forEach { (initial, contactsForInitial) ->
                    stickyHeader {
                        ProjectListHeader(label = initial)
                    }

                    items(contactsForInitial) { contact ->
                        ProjectListItem(
                            label = contact,
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

}
