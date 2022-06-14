package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.ui.theme.ReportCardTypography

@Composable
fun BillableTypeRow(
    color: Color,
    title: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(start = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ReportScreenColorSquare(color = color)
            Text(
                text = title,
                style = ReportCardTypography.subtitle1,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        Text(text = value, style = ReportCardTypography.subtitle2)
    }
}

@Composable
fun ReportScreenColorSquare(color: Color) {
    Surface(
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier.size(8.dp),
        color = color
    ) {}
}