package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.ui.theme.ReportCardTypography


enum class ReportCardWeekType {
    LastWeek, ThisWeek, Week
}

@Composable
fun ReportCards(
    weekOffset: Int,
    weekType: ReportCardWeekType = when (weekOffset) {
        -1 -> ReportCardWeekType.LastWeek
        0 -> ReportCardWeekType.ThisWeek
        else -> ReportCardWeekType.Week
    }
) {
    WeekCard(
        weekName = weekType.name.toCharArray().toMutableList().apply {
            // Add space between two words e.g. 'LastWeek' becomes 'Last Week'
            val indexToAddSpaceTo = indexOfLast { char -> char == 'W' }
            if (indexToAddSpaceTo != 0) {
                add(indexToAddSpaceTo, ' ')
            }
        }.joinToString(separator = "")
    )
    TotalHoursCard(totalHours = 40.00f)
    BillableHoursCard(
        percentage = 100,
        billableHours = 40.00f,
        nonBillableHours = 0.00f
    )
    BillableAmountCard()
    UnInvoicedAmountCard()
    DividedCard(title = "Projects") {
        Column {
            Text(
                text = "Mutual Mobile",
                style = ReportCardTypography.caption,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .alpha(0.5f)
            )
            Text(text = "Android Department Work HYD", style = ReportCardTypography.h4)
        }
    }
    DividedCard(title = "Tasks") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ReportScreenColorSquare(color = Color.Green)
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Text(text = "Work", style = ReportCardTypography.h4)
        }
    }
}
