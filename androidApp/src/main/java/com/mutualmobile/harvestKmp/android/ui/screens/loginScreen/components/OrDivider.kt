package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

private const val DividerAlpha = 0.25f
private const val DividerThickness = 2

@Composable
fun OrDivider() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dividerColor = MaterialTheme.colors.surface
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .alpha(DividerAlpha),
            color = dividerColor,
            thickness = DividerThickness.dp
        )
        Text(text = "or", color = dividerColor, style = MaterialTheme.typography.body2)
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .alpha(DividerAlpha),
            color = dividerColor,
            thickness = DividerThickness.dp
        )
    }
}