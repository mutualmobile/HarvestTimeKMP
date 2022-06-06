package com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.BucketSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.DateDurationSelector
import com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.ProjectListActivity

val SELECTED_PROJECT = "SELECTED_PROJECT"

@Composable
fun NewEntryScreen() {
    val activity = LocalContext.current as Activity
    val selectedProject = remember { mutableStateOf("Android Department Work HYD") }

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedProject.value = (result.data?.extras?.get(SELECTED_PROJECT) ?: "") as String
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
                        onClick = {},
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    ) {
                        Text(
                            text = stringResource(MR.strings.new_entry_screen_start_timer_btn_txt.resourceId),
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

                    startForResult.launch(
                        Intent(
                            activity,
                            ProjectListActivity::class.java
                        )
                    )
                },
                onWorkClick = {})
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            DateDurationSelector()
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            Text(
                text = stringResource(MR.strings.new_entry_screen_end_view_text.resourceId),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }

}



