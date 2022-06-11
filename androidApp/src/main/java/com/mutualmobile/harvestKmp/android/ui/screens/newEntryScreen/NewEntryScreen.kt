package com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.BucketSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.DateDurationSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.serverDateFormatter
import com.mutualmobile.harvestKmp.android.ui.utils.isAFloat
import com.mutualmobile.harvestKmp.android.ui.utils.toDecimalString
import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.WorkRequestType
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLogginDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Date
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun NewEntryScreen(
    navController: NavController,
    newEntryScreenViewModel: NewEntryScreenViewModel = get()
) {
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()
    val currentWorkRequest = newEntryScreenViewModel.currentWorkRequest

    var durationEtText: String by remember {
        mutableStateOf(
            currentWorkRequest?.workHours?.toDecimalString() ?: ""
        )
    }
    var selectedWorkDate: Date by remember {
        mutableStateOf(currentWorkRequest?.workDate?.let { nnWorkDate ->
            serverDateFormatter.parse(nnWorkDate)
        } ?: Date())
    }
    var noteEtText: String by remember { mutableStateOf(currentWorkRequest?.note.orEmpty()) }

    val selectedProjectName = newEntryScreenViewModel.currentProjectName
    val selectedProjectId: String? = currentWorkRequest?.projectId

    BackHandler {
        newEntryScreenViewModel.resetAllItems {
            navController.navigateUp()
        }
    }

    var user: GetUserResponse? by remember { mutableStateOf(null) }
    remember {
        mutableStateOf(
            GetUserDataModel().apply {
                this.dataFlow.onEach { userState ->
                    when (userState) {
                        is SuccessState<*> -> {
                            user = (userState.data) as GetUserResponse
                        }
                        else -> Unit
                    }
                }.launchIn(this.dataModelScope)
                activate()
            }
        )
    }

    var currentLogWorkTimeState: DataState by remember { mutableStateOf(EmptyState) }
    var logWorkTimeNavigationCommands: PraxisCommand? by remember { mutableStateOf(null) }
    val logWorkTimeDataModel by remember {
        mutableStateOf(
            TimeLogginDataModel().apply {
                praxisCommand.onEach { newCommand ->
                    logWorkTimeNavigationCommands = newCommand
                }.launchIn(dataModelScope)
            }
        )
    }

    LaunchedEffect(Unit) {
        if (selectedProjectName.isBlank() && currentWorkRequest != null) {
            newEntryScreenViewModel.fetchProjectName(currentWorkRequest.projectId)
        }
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
                    IconButton(onClick = {
                        activity.onBackPressed()
                    }) {
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
                            coroutineScope.launch {
                                user?.let { nnUser ->
                                    nnUser.id?.let { nnUserId ->
                                        selectedProjectId?.let { nnSelectedProjectId ->
                                            when (newEntryScreenViewModel.currentWorkRequestType) {
                                                WorkRequestType.CREATE -> {
                                                    logWorkTimeDataModel.logWorkTime(
                                                        HarvestUserWorkRequest(
                                                            projectId = nnSelectedProjectId,
                                                            userId = nnUserId,
                                                            workDate = serverDateFormatter.format(
                                                                selectedWorkDate
                                                            ),
                                                            workHours = durationEtText.toFloat(),
                                                            note = noteEtText
                                                        )
                                                    ).collect { logWorkTimeState ->
                                                        currentLogWorkTimeState = logWorkTimeState
                                                    }
                                                }
                                                // TODO: Check why UPDATE is not working while CREATE is
                                                WorkRequestType.UPDATE -> {
                                                    newEntryScreenViewModel.currentWorkRequest?.let { nnCurrentWorkRequest ->
                                                        logWorkTimeDataModel.logWorkTime(
                                                            HarvestUserWorkRequest(
                                                                id = nnCurrentWorkRequest.id,
                                                                projectId = nnSelectedProjectId,
                                                                userId = nnUserId,
                                                                workDate = serverDateFormatter.format(
                                                                    selectedWorkDate
                                                                ),
                                                                workHours = durationEtText.toFloat(),
                                                                note = noteEtText
                                                            )
                                                        ).collect { logWorkTimeState ->
                                                            currentLogWorkTimeState =
                                                                logWorkTimeState
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
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
                currentProject = selectedProjectName,
                onDepartmentClick = {
                    navController.navigate(HarvestRoutes.Screen.ORG_PROJECTS)
                },
                onWorkClick = {},
                note = noteEtText,
                onNoteChanged = { updatedText -> noteEtText = updatedText }
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            DateDurationSelector(
                durationEtText = durationEtText,
                onDurationChange = { duration ->
                    durationEtText = if (duration.isNotBlank() && duration.isAFloat()) {
                        duration
                    } else {
                        ""
                    }
                },
                currentDate = selectedWorkDate,
                onWorkDateChange = { newDate ->
                    selectedWorkDate = newDate
                }
            )
            AnimatedVisibility(visible = currentLogWorkTimeState is LoadingState) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            AnimatedVisibility(visible = currentLogWorkTimeState is ErrorState) {
                Text(
                    text = (currentLogWorkTimeState as ErrorState).throwable.message.orEmpty(),
                    style = MaterialTheme.typography.body2.copy(color = Color.Red)
                )
            }
            Text(
                text = stringResource(MR.strings.new_entry_screen_end_view_text.resourceId),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(0.5f)
            )
        }
        HarvestDialog(praxisCommand = logWorkTimeNavigationCommands, onConfirm = {
            logWorkTimeNavigationCommands = null
            when (currentLogWorkTimeState) {
                is SuccessState<*> -> {
                    newEntryScreenViewModel.resetAllItems {
                        navController.navigateUp()
                    }
                }
                else -> Unit
            }
        })
    }
}