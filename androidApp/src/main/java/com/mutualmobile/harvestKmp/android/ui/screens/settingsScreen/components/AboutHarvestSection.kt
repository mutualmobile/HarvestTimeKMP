package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mutualmobile.harvestKmp.android.BuildConfig
import com.mutualmobile.harvestKmp.MR

@Composable
fun AboutHarvestSection() {
    ColoredText(text = stringResource(MR.strings.about_harvest_section_title.resourceId))
    SettingsListItem(
        title = stringResource(MR.strings.about_harvest_section_open_source_item_title.resourceId),
        subtitle = stringResource(MR.strings.about_harvest_section_open_source_item_subtitle.resourceId),
        showBottomDivider = true,
    )
    SettingsListItem(
        title = stringResource(MR.strings.about_harvest_section_app_version_item_title.resourceId),
        subtitle = stringResource(
            id = MR.strings.about_harvest_section_app_version_item_subtitle.resourceId,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        ),
        showBottomDivider = true,
    )
}