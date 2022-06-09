package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes

@Composable
fun AccountSection(navController: NavHostController) {
    ColoredText(text = stringResource(MR.strings.account_section_title.resourceId))
    SettingsListItem(title = stringResource(MR.strings.account_section_refer_friend_item_title.resourceId))
    SettingsListItem(
        title = stringResource(MR.strings.account_section_email_support_item_title.resourceId),
        showTopDivider = true
    )
    SettingsListItem(
        title = stringResource(MR.strings.account_section_help_center_item_title.resourceId),
        showTopDivider = true
    )
    SettingsListItem(
        title = stringResource(MR.strings.account_section_switch_accounts_item_title.resourceId),
        showTopDivider = true
    )
    SettingsListItem(
        title = stringResource(MR.strings.account_section_signout_item_title.resourceId),
        showTopDivider = true,
        onClick = {
            navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE
        }
    )
    SettingsListItem(
        title = stringResource(MR.strings.change_password.resourceId),
        showTopDivider = true,
        onClick = {navController.navigate(ScreenList.ChangePasswordScreen())}
    )
}