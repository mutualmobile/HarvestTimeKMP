package com.mutualmobile.harvestKmp.android.ui.screens.projectScreen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.harvest.FindProjectsInOrgDataModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectScreen() {
    val activity = LocalContext.current as Activity
    var listMap =
        mapOf("P" to listOf("Praxis", "PraxisFlutter"), "M" to listOf("MagicMountain", "Treat"))
    var filteredMap: Map<String, List<String>>
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    var currentProjectScreenState: DataState by remember {
        mutableStateOf(EmptyState)
    }
    val findProjectsInOrgDataModel by remember {
        mutableStateOf(
            FindProjectsInOrgDataModel { projectState ->
                currentProjectScreenState = projectState
                when (projectState) {
                    is SuccessState<*> -> {
                        val apiResponse = (projectState.data as ApiResponse<*>)
                        listMap = mapOf(
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
                filteredMap = if (searchedText.isEmpty()) {
                    listMap
                } else {
                    var resultList1 = emptyMap<String, List<String>>()

                    listMap.filter { (key, value) ->
                        val list = value.filter { it.contains(searchedText, true) }
                        if (list.isEmpty().not()) {
                            resultList1 = resultList1 + Pair(key, list)
                        }

                        resultList1.isEmpty()


                    }
                    resultList1
                }

                filteredMap.forEach { (initial, contactsForInitial) ->
                    stickyHeader {
                        ProjectListHeader(label = initial)
                    }

                    items(contactsForInitial) { contact ->
                        ProjectListItem(
                            label = contact,
                            onItemClick = { selectedProject ->
                                activity.setResult(
                                    Activity.RESULT_OK,
                                    Intent().putExtra(SELECTED_PROJECT, selectedProject)
                                )
                                activity.finish()
                            })
                    }
                }
            }

        }
    }

}
