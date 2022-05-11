package com.mutualmobile.harvestKmp.android.ui.screens.timeScreen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.NewEntryActivity
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekDays
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekScroller
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.currentPageIndex
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.floorMod
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.targetPageIndex
import com.mutualmobile.harvestKmp.android.ui.theme.SurfaceColor
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

const val MaxItemFling = 1

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun TimeScreen(
    onWeekScrolled: (Int) -> Unit,
    onDayScrolled: (Int) -> Unit,
) {
    val ctx = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                ctx.startActivity(Intent(ctx, NewEntryActivity::class.java))
            }, modifier = Modifier.navigationBarsPadding()) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colors.surface
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val numberOfDays by remember { mutableStateOf(WeekDays.values().size) }

            val startIndex by remember { mutableStateOf(Int.MAX_VALUE.div(2)) }

            val pagerState = rememberPagerState(initialPage = startIndex)

            val lazyRowState = rememberLazyListState(initialFirstVisibleItemIndex = 1)

            val lazyRowFlingBehavior = rememberSnapperFlingBehavior(
                lazyListState = lazyRowState,
                snapIndex = { _, snapperStartIndex, targetIndex ->
                    targetIndex.coerceIn(
                        snapperStartIndex - MaxItemFling,
                        snapperStartIndex + MaxItemFling
                    )
                }
            )

            val layoutInfo = rememberLazyListSnapperLayoutInfo(lazyRowState)

            var currentWeekOffset by remember { mutableStateOf(0) }

            LaunchedEffect(lazyRowState.isScrollInProgress) {
                if (!lazyRowState.isScrollInProgress) {
                    when (layoutInfo.currentItem?.index) {
                        0 -> currentWeekOffset -= 1
                        2 -> currentWeekOffset += 1
                        else -> Unit
                    }
                    lazyRowState.scrollToItem(1)
                }
            }

            LaunchedEffect(currentWeekOffset) {
                onWeekScrolled(currentWeekOffset)
            }

            LaunchedEffect(pagerState.currentPage) {
                val daysOffset = pagerState.currentPage - startIndex
                onDayScrolled(daysOffset)
            }

            LaunchedEffect(pagerState.isScrollInProgress) {
                if (
                    pagerState.currentPageIndex(startIndex) == 0 &&
                    pagerState.targetPageIndex(startIndex) == numberOfDays - 1
                ) {
                    if (!lazyRowState.isScrollInProgress) {
                        lazyRowState.animateScrollToItem(0)
                    }
                }
                if (
                    pagerState.currentPageIndex(startIndex) == numberOfDays - 1 &&
                    pagerState.targetPageIndex(startIndex) == 0
                ) {
                    if (!lazyRowState.isScrollInProgress) {
                        lazyRowState.animateScrollToItem(2, 100)
                    }
                }
            }

            LazyRow(
                state = lazyRowState,
                flingBehavior = lazyRowFlingBehavior,
            ) {
                items(3) {
                    WeekScroller(pagerState, startIndex)
                }
            }

            HorizontalPager(
                count = Int.MAX_VALUE,
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) { currentPageIndex ->
                Text(
                    text = WeekDays.values()[(currentPageIndex - startIndex).floorMod(numberOfDays)].name,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceColor)
                )
            }
        }
    }
}