package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components.AboutHarvestSection
import com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components.AccountSection
import com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components.NotificationsSection

@Composable
fun SettingsScreen(navController: NavHostController) {
    val activity = LocalContext.current as Activity
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = activity::onBackPressed) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = MR.strings.title_activity_settings.resourceId),
                        fontWeight = FontWeight.Bold,
                    )
                },
                contentPadding = WindowInsets.Companion.statusBars.asPaddingValues(),
            )
        },
    ) { bodyPadding ->
        Column(
            modifier = Modifier
                .padding(bodyPadding)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
        ) {
            NotificationsSection()
            AccountSection(navController)
            AboutHarvestSection()
        }
    }
}