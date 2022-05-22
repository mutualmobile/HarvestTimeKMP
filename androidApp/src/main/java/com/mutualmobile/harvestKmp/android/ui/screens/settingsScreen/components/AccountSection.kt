package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mutualmobile.harvestKmp.android.R

@Composable
fun AccountSection() {
    ColoredText(text = stringResource(R.string.account_section_title))
    SettingsListItem(title = stringResource(R.string.account_section_refer_friend_item_title))
    SettingsListItem(
        title = stringResource(R.string.account_section_email_support_item_title),
        showTopDivider = true
    )
    SettingsListItem(
        title = stringResource(R.string.account_section_help_center_item_title),
        showTopDivider = true
    )
    SettingsListItem(
        title = stringResource(R.string.account_section_switch_accounts_item_title),
        showTopDivider = true
    )
    SettingsListItem(
        title = stringResource(R.string.account_section_signout_item_title),
        showTopDivider = true
    )
}