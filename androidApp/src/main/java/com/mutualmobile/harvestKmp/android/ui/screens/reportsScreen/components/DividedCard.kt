package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.ui.theme.ReportCardTypography

@Composable
fun DividedCard(
    title: String,
    content: @Composable () -> Unit
) {
    ReportScreenCard {
        Column {
            Text(text = title, style = ReportCardTypography.h3)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(0.48f)) {
                    content()
                }
                Column(modifier = Modifier.fillMaxWidth(0.98f)) {
                    DividedCardRow(title = "Hours", value = "40.00")
                    DividedCardRow(title = "Billable hours", value = "40.00")
                }
            }
        }
    }
}

@Composable
private fun DividedCardRow(title: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
    ) {
        Text(text = title, style = ReportCardTypography.subtitle1)
        Text(text = value, style = ReportCardTypography.subtitle2)
    }
}