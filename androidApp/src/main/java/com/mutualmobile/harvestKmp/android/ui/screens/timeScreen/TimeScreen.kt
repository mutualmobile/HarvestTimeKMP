package com.mutualmobile.harvestKmp.android.ui.screens.timeScreen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.NewEntryActivity
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.TimeCard
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekDays
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekScroller
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.currentPageIndex
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.floorMod
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.targetPageIndex
import com.mutualmobile.harvestKmp.android.ui.theme.SurfaceColor
import com.mutualmobile.harvestKmp.android.ui.theme.TimeScreenTypography
import dev.chrisbanes.snapper.ExperimentalSnapperApi
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

            LaunchedEffect(lazyRowFlingBehavior.animationTarget) {
                lazyRowFlingBehavior.animationTarget?.let { nnVal ->
                    when (nnVal) {
                        0 -> {
                            onWeekScrolled(-1)
                            pagerState.scrollToPage(pagerState.currentPage - 7)
                        }
                        2 -> {
                            onWeekScrolled(1)
                            pagerState.scrollToPage(pagerState.currentPage + 7)
                        }
                        else -> Unit
                    }
                }
            }

            LaunchedEffect(lazyRowState.isScrollInProgress) {
                if (!lazyRowState.isScrollInProgress) {
                    lazyRowState.scrollToItem(1)
                }
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
                val currentIndex = (currentPageIndex - startIndex).floorMod(numberOfDays)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .background(SurfaceColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentIndex % 2 == 0) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                TimeCard(
                                    organisationName = "Mutual Mobile",
                                    bucketName = "Android Department Work HYD",
                                    time = 8.00f,
                                    taskType = "Work"
                                )
                            }
                            item { Spacer(modifier = Modifier.navigationBarsPadding()) }
                        }
                    } else {
                        Text(
                            text = buildAnnotatedString {
                                append("\"All we have to decide is what to do with the time that is given us.\"")
                                withStyle(SpanStyle(fontSize = 16.sp)) {
                                    append("\n-- Gandalf")
                                }
                            },
                            style = TimeScreenTypography.h1,
                            modifier = Modifier.alpha(0.25f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}