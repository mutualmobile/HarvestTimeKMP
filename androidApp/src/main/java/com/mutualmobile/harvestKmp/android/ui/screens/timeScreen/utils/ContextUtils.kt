package com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.TabPosition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekDays
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@SuppressLint("UnnecessaryComposedModifier")
@OptIn(ExperimentalPagerApi::class)
fun Modifier.pagerTabIndicatorOffsetForWeek(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    indexOffset: Int,
): Modifier = composed {
    // If there are no pages, nothing to show
    if (pagerState.pageCount == 0) return@composed this

    val targetIndicatorOffset: Dp
    val indicatorWidth: Dp

    val currentPage = pagerState.currentPageIndex(
        indexOffset = indexOffset,
    )

    val currentTab = tabPositions[minOf(tabPositions.lastIndex, currentPage)]
    val targetPage = pagerState.targetPageIndex(indexOffset)
    val targetTab = tabPositions.getOrNull(targetPage)

    if (targetTab != null) {
        // The distance between the target and current page. If the pager is animating over many
        // items this could be > 1
        val targetDistance = (targetPage - currentPage).absoluteValue
        // Our normalized fraction over the target distance
        val fraction = (pagerState.currentPageOffset / max(targetDistance, 1)).absoluteValue

        targetIndicatorOffset = lerp(currentTab.left, targetTab.left, fraction)
        indicatorWidth = lerp(currentTab.width, targetTab.width, fraction).absoluteValue
    } else {
        // Otherwise we just use the current tab/page
        targetIndicatorOffset = currentTab.left
        indicatorWidth = currentTab.width
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = targetIndicatorOffset)
        .width(indicatorWidth)
}

private inline val Dp.absoluteValue: Dp
    get() = value.absoluteValue.dp

fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}

private val currentDayName = Clock
    .System
    .now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .dayOfWeek
    .name
    .substring(startIndex = 0, endIndex = 3)

val currentDayIndex = WeekDays.valueOf(currentDayName).ordinal

val numberOfWeekDays = WeekDays.values().size

@OptIn(ExperimentalPagerApi::class)
fun PagerState.currentPageIndex(
    indexOffset: Int,
    numberOfDays: Int = numberOfWeekDays,
) = (currentPage - indexOffset + currentDayIndex).floorMod(numberOfDays)

@OptIn(ExperimentalPagerApi::class)
fun PagerState.targetPageIndex(
    indexOffset: Int,
    numberOfDays: Int = numberOfWeekDays,
) = (targetPage - indexOffset + currentDayIndex).floorMod(numberOfDays)