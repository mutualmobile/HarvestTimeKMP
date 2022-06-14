package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.ui.theme.ReportCardTypography
import com.mutualmobile.harvestKmp.android.ui.utils.toDecimalString

@Composable
fun TotalHoursCard(totalHours: Float) {
    ReportScreenCard {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(MR.strings.reports_screen_total_hours_card_title.resourceId))
            Text(text = totalHours.toDecimalString(), style = ReportCardTypography.h2)
        }
    }
}