package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components.LandingScreenDrawerItemType
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LandingScreenViewModel : ViewModel() {
    var currentDrawerScreen by mutableStateOf(LandingScreenDrawerItemType.Time)
    var currentDayOffset by mutableStateOf(0)
    var localWeekOffset by mutableStateOf(0)

    val currentDay by derivedStateOf {
        val currentDateTime = Clock.System.now().plus(
            currentDayOffset.days
        ).toLocalDateTime(TimeZone.currentSystemDefault())
        "${currentDateTime.dayOfWeek.name}, ${currentDateTime.dayOfMonth} ${currentDateTime.month.name}"
    }

    val timeScreenStartIndex = Int.MAX_VALUE.div(2)
    var currentWeekLogsTotalTime by mutableStateOf("0.00")
    var isWorkLoading by mutableStateOf(false)
}