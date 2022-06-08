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
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.BucketSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.DateDurationSelector
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.LogWorkTimeDataModel

@Composable
fun NewEntryScreen(navController: NavController) {
    val activity = LocalContext.current as Activity
    val durationEtText = remember { mutableStateOf(0.0f) }


    var currentLogWorkTimeState: DataState by remember {
        mutableStateOf(EmptyState)
    }

    val logWorkTimeDataModel by remember {
        mutableStateOf(
            LogWorkTimeDataModel { logWorkTimeState ->
                currentLogWorkTimeState = logWorkTimeState
                when (logWorkTimeState) {
                    is SuccessState<*> -> {
                        navController.popBackStack()
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
                                "ec397b08-b99f-44ac-ba16-c679cddff84c",
                                "ec397b08-b99f-44ac-ba16-c679cddff84c",
                                "ec397b08-b99f-44ac-ba16-c679cddff84c",
                                "2022-06-06",
                                durationEtText.value,
                                "jhhj"
                            )
                        },
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    ) {
                        Text(
                            text = if (durationEtText.value == 0.0f)
                                stringResource(MR.strings.new_entry_screen_start_timer_btn_txt.resourceId)
                            else
                                stringResource(MR.strings.save.resourceId),
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
            BucketSelector()
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            DateDurationSelector(onDurationChange = { duration ->
                durationEtText.value = duration.toFloat()
            })
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            Text(
                text = stringResource(MR.strings.new_entry_screen_end_view_text.resourceId),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}