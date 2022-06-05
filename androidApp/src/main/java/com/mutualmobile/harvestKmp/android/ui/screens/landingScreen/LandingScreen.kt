package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
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
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components.LandingScreenDrawer
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components.LandingScreenDrawerItemType
import com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.ReportsScreen
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.TimeScreen
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components.WeekDays
import com.mutualmobile.harvestKmp.android.ui.theme.DrawerBgColor
import com.mutualmobile.harvestKmp.android.ui.theme.SurfaceColor
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LandingScreen(navController: NavHostController) {
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
                    navController.navigate(ScreenList.SettingsScreen())
                }
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
                    onWeekScrolled = { weekOffset ->
                        currentDayOffset += weekOffset.times(WeekDays.values().size)
                    },
                    onDayScrolled = { dayOffset ->
                        currentDayOffset = dayOffset
                    },
                )
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