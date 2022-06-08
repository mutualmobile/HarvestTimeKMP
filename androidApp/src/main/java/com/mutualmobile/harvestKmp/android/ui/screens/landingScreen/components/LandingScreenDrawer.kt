package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList

@Composable
fun LandingScreenDrawer(
    navController: NavController,
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

    SettingsButton(navController)
}

@Composable
private fun SettingsButton(navController: NavController) {
    Text(
        text = stringResource(id = MR.strings.drawer_settings_btn_txt.resourceId),
        style = MaterialTheme.typography.subtitle2.copy(
            color = MaterialTheme.colors.surface.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(ScreenList.SettingsScreen())
            }
            .padding(16.dp),
    )
}
