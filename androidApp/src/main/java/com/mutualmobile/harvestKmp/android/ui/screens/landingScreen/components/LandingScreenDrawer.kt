package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.SettingsActivity

@Composable
fun LandingScreenDrawer(
    currentDrawerScreen: LandingScreenDrawerItemType,
    closeDrawer: () -> Unit,
    onScreenChanged: (LandingScreenDrawerItemType) -> Unit,
) {
    UserInfoSection()

    LandingScreenDrawerItemType.values().forEach { drawerItem ->
        LandingScreenDrawerItem(
            itemType = drawerItem,
            isSelected = currentDrawerScreen == drawerItem
        ) {
            onScreenChanged(drawerItem)
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
    val activity = LocalContext.current as Activity
    Text(
        text = stringResource(id = R.string.drawer_settings_btn_txt),
        style = MaterialTheme.typography.subtitle2.copy(
            color = MaterialTheme.colors.surface.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                activity.startActivity(Intent(activity, SettingsActivity::class.java))
            }
            .padding(16.dp),
    )
}
