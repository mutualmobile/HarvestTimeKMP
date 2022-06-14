package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.ui.theme.PrimaryLightColor

@Composable
fun ColoredText(text: String) {
    Text(
        text = text,
        color = PrimaryLightColor,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        style = MaterialTheme.typography.body2,
        fontWeight = FontWeight.SemiBold
    )
}