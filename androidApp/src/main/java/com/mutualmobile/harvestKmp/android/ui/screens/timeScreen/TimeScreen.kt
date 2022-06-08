package com.mutualmobile.harvestKmp.android.ui.screens.timeScreen

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.TimeCard
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekDays
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekScroller
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.currentDayIndex
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.currentPageIndex
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.floorMod
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.targetPageIndex
import com.mutualmobile.harvestKmp.android.ui.theme.SurfaceColor
import com.mutualmobile.harvestKmp.android.ui.theme.TimeScreenTypography
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLogginDataModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

const val MaxItemFling = 1

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun TimeScreen(
    pagerState: PagerState,
    startIndex: Int,
    onWeekScrolled: (Int) -> Unit,
    onDayScrolled: (Int) -> Unit,
    goToNewEntryScreen: () -> Unit,
    isWorkLoading: (Boolean) -> Unit,
    navigateToFindWorkspaceScreen: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = goToNewEntryScreen,
                modifier = Modifier.navigationBarsPadding()
            ) {
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
            var localWeekOffset by remember { mutableStateOf(0) }

            var getUserState: DataState by remember { mutableStateOf(EmptyState) }
            val getUserDataModel by remember { mutableStateOf(
                GetUserDataModel { newState ->
                    getUserState = newState
                }.activate()
            ) }

            //TODO: Make this business logic common/shared
            val dateRangeStart by remember(localWeekOffset) {
                mutableStateOf(
                    Clock.System.now()
                        .minus(currentDayIndex.days)
                        .plus((localWeekOffset * 7).days)
                )
            }
            val dateRangeEnd by remember(localWeekOffset) {
                mutableStateOf(
                    Clock.System.now()
                        .plus((WeekDays.values().lastIndex - currentDayIndex).days)
                        .plus((localWeekOffset * 7).days)
                )
            }

            var currentWeekWorkLogs by remember {
                mutableStateOf(emptyList<HarvestUserWorkResponse>())
            }
            var timeLoggingPraxisCommand: PraxisCommand? by remember { mutableStateOf(null) }
            val timeLoggingDataModel by remember { mutableStateOf(TimeLogginDataModel().apply {
                praxisCommand = { newCommand ->
                    timeLoggingPraxisCommand = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            if (newCommand.screen.isBlank()) {
                                navigateToFindWorkspaceScreen()
                            }
                        }
                    }
                }
            }) }

            LaunchedEffect(localWeekOffset, getUserState) {
                when (getUserState) {
                    is SuccessState<*> -> {
                        (getUserState as? SuccessState<GetUserResponse>)?.data?.id?.let { nnUserId ->
                            timeLoggingDataModel.getWorkLogsForDateRange(
                                startDate = dateRangeStart.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                                endDate = dateRangeEnd.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                                userIds = listOf(nnUserId)
                            ).collect { newState ->
                                currentWeekWorkLogs = emptyList()
                                isWorkLoading(newState is LoadingState)
                                when (newState) {
                                    is SuccessState<*> -> {
                                        (newState as? SuccessState<ApiResponse<List<HarvestUserWorkResponse>>>)
                                            ?.data?.data?.let { apiWorkLogs ->
                                                currentWeekWorkLogs = apiWorkLogs
                                            }
                                    }
                                    else -> Unit
                                }
                            }
                        }
                    }
                    else -> Unit
                }
            }

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
                            localWeekOffset -= 1
                        }
                        2 -> {
                            onWeekScrolled(1)
                            pagerState.scrollToPage(pagerState.currentPage + 7)
                            localWeekOffset += 1
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
                    localWeekOffset -= 1
                    if (!lazyRowState.isScrollInProgress) {
                        lazyRowState.animateScrollToItem(0)
                    }
                }
                if (
                    pagerState.currentPageIndex(startIndex) == numberOfDays - 1 &&
                    pagerState.targetPageIndex(startIndex) == 0
                ) {
                    localWeekOffset += 1
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
                val currentIndex by remember(currentPageIndex) {
                    derivedStateOf {
                        (currentPageIndex - startIndex + currentDayIndex).floorMod(numberOfDays)
                    }
                }
                val currentPageDate by remember(currentIndex) {
                    mutableStateOf(dateRangeStart
                        .plus(currentIndex.days)
                        .toLocalDateTime(TimeZone.currentSystemDefault()))
                }
                val currentPageWorkLogs by remember(currentWeekWorkLogs, currentPageDate) {
                    mutableStateOf(
                        currentWeekWorkLogs.filter { work ->
                            work.workDate.substring(0, 10) == currentPageDate.toString().substring(0, 10)
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .background(SurfaceColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentPageWorkLogs.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            currentPageWorkLogs.forEach { workLog ->
                                item {
                                    TimeCard(
                                        organisationName = "Mutual Mobile",
                                        bucketName = "Android Department Work HYD",
                                        time = workLog.workHours,
                                        taskType = "Work"
                                    )
                                }
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
            HarvestDialog(praxisCommand = timeLoggingPraxisCommand, onConfirm = {
                timeLoggingPraxisCommand = null
            })
        }
    }
}