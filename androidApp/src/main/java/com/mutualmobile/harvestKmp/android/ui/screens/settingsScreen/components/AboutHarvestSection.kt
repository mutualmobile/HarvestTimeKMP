package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mutualmobile.harvestKmp.android.BuildConfig
import com.mutualmobile.harvestKmp.android.R

@Composable
fun AboutHarvestSection() {
    ColoredText(text = stringResource(R.string.about_harvest_section_title))
    SettingsListItem(
        title = stringResource(R.string.about_harvest_section_open_source_item_title),
        subtitle = stringResource(R.string.about_harvest_section_open_source_item_subtitle),
        showBottomDivider = true,
    )
    SettingsListItem(
        title = stringResource(R.string.about_harvest_section_app_version_item_title),
        subtitle = stringResource(
            id = R.string.about_harvest_section_app_version_item_subtitle,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        ),
        showBottomDivider = true,
    )
}