package com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.currentPageIndex
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.pagerTabIndicatorOffsetForWeek
import com.mutualmobile.harvestKmp.android.ui.utils.toDecimalString
import kotlinx.coroutines.launch

enum class WeekDays {
    SAT, SUN, MON, TUE, WED, THU, FRI
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WeekScroller(pagerState: PagerState, indexOffset: Int, listOfWeekDayWorkHours: List<Float>) {
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp
        .plus(0.5f) // Because screenWidthDp calculates the size as half a dp less
        .dp
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(0)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPageIndex(indexOffset = indexOffset),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffsetForWeek(
                        pagerState = pagerState,
                        tabPositions = tabPositions,
                        indexOffset = indexOffset,
                    ),
                    color = MaterialTheme.colors.primary,
                    height = 3.dp
                )
            },
            modifier = Modifier.width(screenWidth),
            backgroundColor = MaterialTheme.colors.surface,
        ) {
            WeekDays.values().forEachIndexed { index, weekDay ->
                Tab(
                    selected = pagerState.currentPageIndex(indexOffset = indexOffset) == index,
                    onClick = {
                        coroutineScope.launch {
                            val pageToScroll =
                                pagerState.currentPage + (index - pagerState.currentPageIndex(
                                    indexOffset = indexOffset
                                ))
                            pagerState.animateScrollToPage(pageToScroll)
                        }
                    },
                    unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
                ) {
                    Text(
                        text = weekDay.name,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        listOfWeekDayWorkHours[index].toDecimalString(),
                        style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Light),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}