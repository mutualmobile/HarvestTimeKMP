package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mutualmobile.harvestKmp.MR

@Composable
fun NotificationsSection() {
    ColoredText(text = stringResource(MR.strings.notifications_section_title.resourceId))
    SettingsListItem(
        title = stringResource(MR.strings.notifications_section_running_timer_item_title.resourceId),
        subtitle = stringResource(MR.strings.notifications_section_running_timer_item_subtitle.resourceId),
        isCheckable = true,
    )
    SettingsListItem(
        title = stringResource(MR.strings.notifications_section_reminders_item_title.resourceId),
        subtitle = stringResource(MR.strings.notifications_section_reminders_item_subtitle.resourceId),
        isCheckable = true,
        showTopDivider = true,
    )
}