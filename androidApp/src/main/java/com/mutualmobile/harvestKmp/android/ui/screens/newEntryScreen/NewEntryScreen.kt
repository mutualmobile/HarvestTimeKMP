package com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.BucketSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.DateDurationSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.formatter
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLogginDataModel
import java.util.*

val SELECTED_PROJECT = "SELECTED_PROJECT"

@Composable
fun NewEntryScreen(navController: NavController) {
    val activity = LocalContext.current as Activity
    val durationEtText = remember { mutableStateOf(0.0f) }
    val selectedProject = remember { mutableStateOf("") }
    val selectedWorkDate = remember { mutableStateOf((Date())) }
    var userResponse = GetUserResponse()
    selectedProject.value =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>(SELECTED_PROJECT)
            ?: "Android Department Work HYD"

    var currentUserState: DataState by remember { mutableStateOf(EmptyState) }

    var currentLogWorkTimeState: DataState by remember {
        mutableStateOf(EmptyState)
    }

    val userStateModel by remember {
        mutableStateOf(
            GetUserDataModel { userState ->
                currentUserState = userState
                when (userState) {
                    is SuccessState<*> -> {
                        userResponse = (userState.data) as GetUserResponse
                    }
                    else -> Unit
                }
            }.apply {
                activate()
            }
        )
    }
    val logWorkTimeDataModel by remember {
        mutableStateOf(
            TimeLogginDataModel { logWorkTimeState ->
                currentLogWorkTimeState = logWorkTimeState
                when (logWorkTimeState) {
                    is SuccessState<*> -> {
                        navController.popBackStack()
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
                        text = stringResource(id = MR.strings.title_activity_new_entry.resourceId),
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
                contentPadding = WindowInsets.Companion.statusBars.asPaddingValues(),
            )
        },
        bottomBar = {
            Surface(elevation = 16.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            logWorkTimeDataModel.logWorkTime(
                                HarvestUserWorkRequest(
                                    id = userResponse.orgId,
                                    projectId = "",
                                    userId = userResponse.id ?: "",
                                    workDate = formatter.format(selectedWorkDate.value),
                                    workHours = durationEtText.value,
                                    note = ""

                                )
                            )
                        },
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    ) {
                        Text(
                            text = stringResource(MR.strings.save.resourceId),
                            style = MaterialTheme.typography.button.copy(
                                color = MaterialTheme.colors.onSurface
                            ),
                        )
                    }
                }
            }
        }
    ) { bodyPadding ->
        Column(
            modifier = Modifier
                .padding(bodyPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BucketSelector(
                currentProject = selectedProject.value,
                onDepartmentClick = {
                    navController.navigate(ScreenList.ProjectScreen())
                },
                onWorkClick = {})
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            DateDurationSelector(
                onDurationChange = { duration ->
                    durationEtText.value = duration.toFloat()
                },
                currentDate = selectedWorkDate.value,
                onWorkDateChange = {
                    selectedWorkDate.value = it
                }
            )
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            Text(
                text = stringResource(MR.strings.new_entry_screen_end_view_text.resourceId),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}