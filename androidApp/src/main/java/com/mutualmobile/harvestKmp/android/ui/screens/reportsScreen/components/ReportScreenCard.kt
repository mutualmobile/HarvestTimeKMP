package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.ui.theme.ReportCardTypography

private val WeekCardShape = RoundedCornerShape(8.dp)

@Composable
fun ReportScreenCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = WeekCardShape,
        elevation = 0.dp
    ) {
        CompositionLocalProvider(LocalTextStyle provides ReportCardTypography.body1) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) { content() }
        }
    }
}