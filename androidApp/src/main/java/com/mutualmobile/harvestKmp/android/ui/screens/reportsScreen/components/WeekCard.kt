package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.mutualmobile.harvestKmp.ui.theme.ReportCardTypography

@Composable
fun WeekCard(
    weekName: String,
    weekRange: String = "30 Apr - 06 May 2022",
) {
    ReportScreenCard {
        Column {
            Text(
                text = weekName,
                style = ReportCardTypography.h1
            )
            Text(
                text = weekRange,
                modifier = Modifier.alpha(0.75f)
            )
        }
    }
}