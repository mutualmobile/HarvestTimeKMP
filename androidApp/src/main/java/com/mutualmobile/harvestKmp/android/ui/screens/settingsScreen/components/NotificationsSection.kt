package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mutualmobile.harvestKmp.android.R

@Composable
fun NotificationsSection() {
    ColoredText(text = stringResource(R.string.notifications_section_title))
    SettingsListItem(
        title = stringResource(R.string.notifications_section_running_timer_item_title),
        subtitle = stringResource(R.string.notifications_section_running_timer_item_subtitle),
        isCheckable = true,
    )
    SettingsListItem(
        title = stringResource(R.string.notifications_section_reminders_item_title),
        subtitle = stringResource(R.string.notifications_section_reminders_item_subtitle),
        isCheckable = true,
        showTopDivider = true,
    )
}