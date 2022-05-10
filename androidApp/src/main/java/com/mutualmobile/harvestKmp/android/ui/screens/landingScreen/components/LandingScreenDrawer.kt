package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.android.R

@Composable
fun LandingScreenDrawer(
    closeDrawer: () -> Unit
) {
    var currentSelectedItem by remember { mutableStateOf(LandingScreenDrawerItemType.Time) }

    UserInfoSection()

    LandingScreenDrawerItemType.values().forEach { drawerItem ->
        LandingScreenDrawerItem(
            itemType = drawerItem,
            isSelected = currentSelectedItem == drawerItem
        ) {
            currentSelectedItem = drawerItem
            closeDrawer()
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(horizontal = 8.dp),
        color = Color.White.copy(alpha = 0.075f),
        thickness = 0.6.dp
    )

    SettingsButton()
}

@Composable
private fun SettingsButton() {
    Text(
        text = stringResource(id = R.string.drawer_settings_btn_txt),
        style = MaterialTheme.typography.subtitle2.copy(
            color = MaterialTheme.colors.surface.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(16.dp),
    )
}
