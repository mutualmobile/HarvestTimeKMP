package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mutualmobile.harvestKmp.android.ui.theme.DrawerItemSelectedColor

enum class LandingScreenDrawerItemType {
    Time, Reports
}

@Composable
fun LandingScreenDrawerItem(
    itemType: LandingScreenDrawerItemType,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
) {
    Text(
        text = itemType.name,
        style = MaterialTheme.typography.subtitle1.copy(
            color = MaterialTheme.colors.surface,
            fontSize = 18.sp
        ),
        modifier = Modifier
            .background(
                if (isSelected) DrawerItemSelectedColor else Color.Transparent
            )
            .clickable(onClick = onItemSelected)
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    )
}