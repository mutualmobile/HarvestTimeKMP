package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.DrawerValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components.LandingScreenDrawer
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components.LandingScreenDrawerItemType
import com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.ReportsScreen
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.TimeScreen
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekDays
import com.mutualmobile.harvestKmp.android.ui.theme.DrawerBgColor
import com.mutualmobile.harvestKmp.android.ui.theme.SurfaceColor
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun LandingScreen(
    navController: NavHostController,
    orgIdentifier: String?,
) {
    val scaffoldDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackBarHostState = remember { SnackbarHostState() }
    val scaffoldState = ScaffoldState(
        drawerState = scaffoldDrawerState,
        snackbarHostState = snackBarHostState
    )
    val coroutineScope = rememberCoroutineScope()
    var currentDrawerScreen by remember { mutableStateOf(LandingScreenDrawerItemType.Time) }
    var currentDayOffset by remember { mutableStateOf(0) }

    val currentDay by remember(currentDayOffset) {
        derivedStateOf {
            val currentDateTime = Clock.System.now().plus(
                currentDayOffset.days
            ).toLocalDateTime(TimeZone.currentSystemDefault())
            "${currentDateTime.dayOfWeek.name}, ${currentDateTime.dayOfMonth} ${currentDateTime.month.name}"
        }
    }
    var isDropDownMenuShown by remember { mutableStateOf(false) }

    val timeScreenStartIndex by remember { mutableStateOf(Int.MAX_VALUE.div(2)) }
    val timeScreenPagerState = rememberPagerState(initialPage = timeScreenStartIndex)

    BackHandler(enabled = scaffoldDrawerState.isOpen) {
        coroutineScope.launch { scaffoldDrawerState.close() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = currentDrawerScreen.name)
                        when (currentDrawerScreen) {
                            LandingScreenDrawerItemType.Time -> {
                                Text(
                                    text = currentDay,
                                    style = MaterialTheme.typography.body2.copy(
                                        color = MaterialTheme.colors.surface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                            LandingScreenDrawerItemType.Reports -> {
                                Text(
                                    text = stringResource(MR.strings.reports_screen_app_bar_subtitle.resourceId),
                                    style = MaterialTheme.typography.body1.copy(
                                        color = MaterialTheme.colors.surface.copy(alpha = 0.6f)
                                    )
                                )
                            }
                        }
                    }
                },
                contentPadding = WindowInsets.Companion.statusBars.asPaddingValues(),
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    when (currentDrawerScreen) {
                        LandingScreenDrawerItemType.Time -> {
                            if (timeScreenPagerState.currentPage != timeScreenStartIndex) {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        timeScreenPagerState.scrollToPage(timeScreenStartIndex)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null
                                    )
                                }
                            }
                            IconButton(
                                onClick = { isDropDownMenuShown = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.surface
                                )
                            }
                            DropdownMenu(
                                expanded = isDropDownMenuShown,
                                onDismissRequest = { isDropDownMenuShown = false },
                            ) {
                                DropdownMenuItem(onClick = {}) {
                                    Text(text = stringResource(MR.strings.landing_screen_dropdown_jump_to_date_option.resourceId))
                                }
                                DropdownMenuItem(onClick = {}) {
                                    Text(text = stringResource(MR.strings.landing_screen_dropdown_submit_week_option.resourceId))
                                }
                                DropdownMenuItem(onClick = {}, enabled = false) {
                                    Text(
                                        text = stringResource(
                                            MR.strings.landing_screen_dropdown_week_total_option.resourceId,
                                            "0.00"
                                        ),
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f)
                                    )
                                }
                            }
                        }
                        LandingScreenDrawerItemType.Reports -> Unit
                    }
                }
            )
        },
        drawerShape = landingScreenDrawerShape(),
        drawerContent = {
            LandingScreenDrawer(
                currentDrawerScreen = currentDrawerScreen,
                closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                onScreenChanged = { newScreen: LandingScreenDrawerItemType ->
                    currentDrawerScreen = newScreen
                },
                goToSettingsScreen = {
                    navController.navigate(HarvestRoutes.Screen.SETTINGS)
                },
                orgIdentifier = orgIdentifier
            )
        },
        drawerBackgroundColor = DrawerBgColor,
        scaffoldState = scaffoldState,
        backgroundColor = SurfaceColor
    ) { bodyPadding ->
        AnimatedContent(
            targetState = currentDrawerScreen,
            transitionSpec = { fadeIn() + fadeIn() with fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(bodyPadding)
        ) { drawerScreenState ->
            when (drawerScreenState) {
                LandingScreenDrawerItemType.Time -> TimeScreen(
                    pagerState = timeScreenPagerState,
                    startIndex = timeScreenStartIndex,
                    onWeekScrolled = { weekOffset ->
                        currentDayOffset += weekOffset.times(WeekDays.values().size)
                    },
                    onDayScrolled = { dayOffset ->
                        currentDayOffset = dayOffset
                    }
                ) {
                    navController.navigate(HarvestRoutes.Screen.WORK_ENTRY)
                }
                LandingScreenDrawerItemType.Reports -> ReportsScreen()
            }
        }
    }
}

private fun landingScreenDrawerShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f, 0f, size.width.times(0.75f), size.height))
    }
}