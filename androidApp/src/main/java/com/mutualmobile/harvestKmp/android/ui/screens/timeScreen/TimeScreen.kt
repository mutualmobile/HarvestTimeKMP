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
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.numberOfWeekDays
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.utils.targetPageIndex
import com.mutualmobile.harvestKmp.android.ui.theme.SurfaceColor
import com.mutualmobile.harvestKmp.android.ui.theme.TimeScreenTypography
import com.mutualmobile.harvestKmp.android.ui.utils.dateWithoutTimeZone
import com.mutualmobile.harvestKmp.android.ui.utils.toDecimalString
import com.mutualmobile.harvestKmp.android.viewmodels.WorkRequestType
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLogginDataModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

const val MaxItemFling = 1

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun TimeScreen(
    localWeekOffset: Int,
    pagerState: PagerState,
    startIndex: Int,
    onWeekScrolled: (Int) -> Unit,
    onDayScrolled: (Int) -> Unit,
    goToNewEntryScreen: (workRequest: HarvestUserWorkRequest?, workRequestType: WorkRequestType) -> Unit,
    isWorkLoading: (Boolean) -> Unit,
    navigateToFindWorkspaceScreen: () -> Unit,
    onWeekOffsetChanged: (Int) -> Unit,
    onUpdateWeekLogsTotalTime: (String) -> Unit
) {
    var getUserState: DataState by remember { mutableStateOf(EmptyState) }
    val getUserDataModel by remember { mutableStateOf(
        GetUserDataModel ().apply {
            this.dataFlow.onEach {
                    newState ->
                getUserState = newState
            }.launchIn(this.dataModelScope)
            this.activate()
        }
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

    val listOfWeekDayWorkHours by remember(currentWeekWorkLogs) {
        mutableStateOf(
            WeekDays.values().map { weekDay ->
                val currentIndex = weekDay.ordinal
                val currentPageDate by mutableStateOf(dateRangeStart
                    .plus(currentIndex.days)
                    .toLocalDateTime(TimeZone.currentSystemDefault()))
                val currentPageWorkLogs by mutableStateOf(
                    currentWeekWorkLogs.filter { work ->
                        work.workDate.dateWithoutTimeZone() == currentPageDate.toString().dateWithoutTimeZone()
                    }
                )
                var totalWorkHours by mutableStateOf(0f)
                currentPageWorkLogs.map { workLog ->
                    totalWorkHours += workLog.workHours
                }
                totalWorkHours
            }
        )
    }

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
                                        var totalWeekTime = 0f
                                        apiWorkLogs.forEach { apiWorkLog ->
                                            totalWeekTime += apiWorkLog.workHours
                                        }
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

    LaunchedEffect(listOfWeekDayWorkHours) {
        onUpdateWeekLogsTotalTime(listOfWeekDayWorkHours.sum().toDecimalString())
    }

    LaunchedEffect(lazyRowFlingBehavior.animationTarget) {
        lazyRowFlingBehavior.animationTarget?.let { nnVal ->
            when (nnVal) {
                0 -> {
                    onWeekScrolled(-1)
                    pagerState.scrollToPage(pagerState.currentPage - 7)
                    onWeekOffsetChanged(-1)
                }
                2 -> {
                    onWeekScrolled(1)
                    pagerState.scrollToPage(pagerState.currentPage + 7)
                    onWeekOffsetChanged(1)
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
            pagerState.targetPageIndex(startIndex) == numberOfWeekDays - 1
        ) {
            onWeekOffsetChanged(-1)
            if (!lazyRowState.isScrollInProgress) {
                lazyRowState.animateScrollToItem(0)
            }
        }
        if (
            pagerState.currentPageIndex(startIndex) == numberOfWeekDays - 1 &&
            pagerState.targetPageIndex(startIndex) == 0
        ) {
            onWeekOffsetChanged(1)
            if (!lazyRowState.isScrollInProgress) {
                lazyRowState.animateScrollToItem(2, 100)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    goToNewEntryScreen(null, WorkRequestType.CREATE)
                },
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
            LazyRow(
                state = lazyRowState,
                flingBehavior = lazyRowFlingBehavior,
            ) {
                items(3) {
                    WeekScroller(
                        pagerState,
                        startIndex,
                        listOfWeekDayWorkHours
                    )
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
                        (currentPageIndex - startIndex + currentDayIndex).floorMod(numberOfWeekDays)
                    }
                }
                val currentPageDate by remember(currentIndex, currentWeekWorkLogs) {
                    mutableStateOf(dateRangeStart
                        .plus(currentIndex.days)
                        .toLocalDateTime(TimeZone.currentSystemDefault()))
                }
                val currentPageWorkLogs by remember(currentWeekWorkLogs, currentPageDate) {
                    mutableStateOf(
                        currentWeekWorkLogs.filter { work ->
                            work.workDate.dateWithoutTimeZone() == currentPageDate.toString().dateWithoutTimeZone()
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
                                        taskType = "Work",
                                        onClick = {
                                            goToNewEntryScreen(
                                                HarvestUserWorkRequest(
                                                    id = workLog.id,
                                                    projectId = workLog.projectId,
                                                    userId = workLog.userId,
                                                    workDate = workLog.workDate,
                                                    workHours = workLog.workHours,
                                                    note = workLog.note
                                                ),
                                                WorkRequestType.UPDATE
                                            )
                                        }
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