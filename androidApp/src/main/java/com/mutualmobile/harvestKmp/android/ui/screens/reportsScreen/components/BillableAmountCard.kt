package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mutualmobile.harvestKmp.ui.theme.ReportCardTypography

@Composable
fun BillableAmountCard() {
    ReportScreenCard {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Billable amount")
            Text(text = "N/A", style = ReportCardTypography.h2)
        }
    }
}