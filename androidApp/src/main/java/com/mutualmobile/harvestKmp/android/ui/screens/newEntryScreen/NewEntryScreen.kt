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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mutualmobile.harvestKmp.android.ui.screens.common.CommonAlertDialog
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.BucketSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.DateDurationSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.serverDateFormatter
import com.mutualmobile.harvestKmp.android.ui.utils.get
import com.mutualmobile.harvestKmp.android.ui.utils.isAFloat
import com.mutualmobile.harvestKmp.android.ui.utils.showToast
import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.WorkRequestType
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun NewEntryScreen(
    navController: NavController,
    nesVm: NewEntryScreenViewModel = get(),
    user: GetUserResponse?
) {
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()
    val currentWorkRequest = nesVm.currentWorkRequest
    val selectedProjectName = nesVm.currentProjectName
    val selectedProjectId: String? = currentWorkRequest?.projectId

    BackHandler {
        nesVm.resetAllItems { navController.navigateUp() }
    }

    LaunchedEffect(Unit) {
        if (selectedProjectName.isBlank() && currentWorkRequest != null) {
            nesVm.fetchProjectName(currentWorkRequest.projectId)
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
                actions = {
                    if (nesVm.currentWorkRequestType == WorkRequestType.UPDATE) {
                        if (nesVm.deleteWorkState is LoadingState) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            IconButton(
                                onClick = { nesVm.isDeleteDialogVisible = true },
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            }
                            when (nesVm.deleteWorkState) {
                                is SuccessState<*> -> navController.navigateUp()
                                is ErrorState -> activity.showToast(
                                    (nesVm.deleteWorkState as ErrorState).throwable.message
                                        ?: "Unexpected Error Occurred!"
                                )
                                else -> Unit
                            }
                        }
                    }
                }
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
                                            when (nesVm.currentWorkRequestType) {
                                                WorkRequestType.CREATE -> {
                                                    nesVm.logWorkTimeDataModel.logWorkTime(
                                                        HarvestUserWorkRequest(
                                                            projectId = nnSelectedProjectId,
                                                            userId = nnUserId,
                                                            workDate = serverDateFormatter.format(
                                                                nesVm.selectedWorkDate
                                                            ),
                                                            workHours = nesVm.durationEtText.toFloat(),
                                                            note = nesVm.noteEtText
                                                        )
                                                    ).collect { logWorkTimeState ->
                                                        nesVm.currentLogWorkTimeState =
                                                            logWorkTimeState
                                                    }
                                                }
                                                WorkRequestType.UPDATE -> {
                                                    nesVm.currentWorkRequest?.let { nnCurrentWorkRequest ->
                                                        nesVm.logWorkTimeDataModel.logWorkTime(
                                                            HarvestUserWorkRequest(
                                                                id = nnCurrentWorkRequest.id,
                                                                projectId = nnSelectedProjectId,
                                                                userId = nnUserId,
                                                                workDate = serverDateFormatter.format(
                                                                    nesVm.selectedWorkDate
                                                                ),
                                                                workHours = nesVm.durationEtText.toFloat(),
                                                                note = nesVm.noteEtText
                                                            )
                                                        ).collect { logWorkTimeState ->
                                                            nesVm.currentLogWorkTimeState =
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
                note = nesVm.noteEtText,
                onNoteChanged = { updatedText -> nesVm.noteEtText = updatedText }
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            DateDurationSelector(
                durationEtText = nesVm.durationEtText,
                onDurationChange = { duration ->
                    nesVm.durationEtText = if (duration.isNotBlank() && duration.isAFloat()) {
                        duration
                    } else {
                        ""
                    }
                },
                currentDate = nesVm.selectedWorkDate,
                onWorkDateChange = { newDate ->
                    nesVm.selectedWorkDate = newDate
                }
            )
            AnimatedVisibility(visible = nesVm.currentLogWorkTimeState is LoadingState) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            AnimatedVisibility(visible = nesVm.currentLogWorkTimeState is ErrorState) {
                Text(
                    text = (nesVm.currentLogWorkTimeState as ErrorState).throwable.message.orEmpty(),
                    style = MaterialTheme.typography.body2.copy(color = Color.Red)
                )
            }
            Text(
                text = stringResource(MR.strings.new_entry_screen_end_view_text.resourceId),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(0.5f)
            )
        }
        HarvestDialog(praxisCommand = nesVm.logWorkTimeNavigationCommands, onConfirm = {
            nesVm.logWorkTimeNavigationCommands = null
            when (nesVm.currentLogWorkTimeState) {
                is SuccessState<*> -> {
                    nesVm.resetAllItems {
                        navController.navigateUp()
                    }
                }
                else -> Unit
            }
        })
    }
    if (nesVm.isDeleteDialogVisible) {
        CommonAlertDialog(
            onDismiss = { nesVm.isDeleteDialogVisible = false },
            onConfirm = { nesVm.deleteWork(onCompleted = { nesVm.isDeleteDialogVisible = false }) },
            titleProvider = { MR.strings.delete_work_dialog_title.get() },
            bodyTextProvider = { MR.strings.delete_work_dialog_bodyText.get() }
        )
    }
}