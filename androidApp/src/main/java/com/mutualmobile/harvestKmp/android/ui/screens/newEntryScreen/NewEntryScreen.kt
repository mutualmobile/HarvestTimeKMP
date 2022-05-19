package com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen

import android.app.Activity
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.BucketSelector
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.DateDurationSelector

@Composable
fun NewEntryScreen() {
    val activity = LocalContext.current as Activity
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_activity_new_entry),
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
                            text = stringResource(R.string.new_entry_screen_start_timer_btn_txt),
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
            DateDurationSelector()
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            Text(
                text = stringResource(R.string.new_entry_screen_end_view_text),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}