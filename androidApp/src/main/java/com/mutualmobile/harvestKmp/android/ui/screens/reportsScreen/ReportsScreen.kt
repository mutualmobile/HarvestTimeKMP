package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components.ReportCards
import com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components.WeekSwitcher

@Composable
fun ReportsScreen() {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
            var weekOffset by remember { mutableStateOf(0) }
            WeekSwitcher(
                onWeekIncreased = { weekOffset += 1 },
                onWeekDecreased = { weekOffset -= 1 },
            )
            ReportCards(weekOffset = weekOffset)
        }
    }
}