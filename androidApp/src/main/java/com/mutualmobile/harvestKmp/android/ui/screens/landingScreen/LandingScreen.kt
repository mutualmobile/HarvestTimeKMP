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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.android.viewmodels.LandingScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.MainActivityViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun LandingScreen(
    navController: NavHostController,
    nesVm: NewEntryScreenViewModel = get(),
    lsVm: LandingScreenViewModel = get(),
    maVm: MainActivityViewModel = get(),
    userOrganization: HarvestOrganization? = null,
    userState: PraxisDataModel.DataState
) {
    val scaffoldDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scaffoldState = ScaffoldState(
        drawerState = scaffoldDrawerState,
        snackbarHostState = lsVm.snackBarHostState
    )
    val coroutineScope = rememberCoroutineScope()

    val timeScreenPagerState = rememberPagerState(initialPage = lsVm.timeScreenStartIndex)

    BackHandler(enabled = scaffoldDrawerState.isOpen) {
        coroutineScope.launch { scaffoldDrawerState.close() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = lsVm.currentDrawerScreen.name)
                        when (lsVm.currentDrawerScreen) {
                            LandingScreenDrawerItemType.Time -> {
                                Text(
                                    text = lsVm.currentDay,
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
                    when (lsVm.currentDrawerScreen) {
                        LandingScreenDrawerItemType.Time -> {
                            if (lsVm.isWorkLoading) {
                                CircularProgressIndicator(
                                    color = Color.White
                                )
                            }
                            if (timeScreenPagerState.currentPage != lsVm.timeScreenStartIndex) {
                                IconButton(onClick = {
                                    lsVm.localWeekOffset = 0
                                    coroutineScope.launch {
                                        timeScreenPagerState.scrollToPage(lsVm.timeScreenStartIndex)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null
                                    )
                                }
                            }
                            IconButton(
                                onClick = { lsVm.isDropDownMenuShown = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.surface
                                )
                            }
                            DropdownMenu(
                                expanded = lsVm.isDropDownMenuShown,
                                onDismissRequest = { lsVm.isDropDownMenuShown = false },
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
                                            lsVm.currentWeekLogsTotalTime
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
                currentDrawerScreen = lsVm.currentDrawerScreen,
                closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                onScreenChanged = { newScreen: LandingScreenDrawerItemType ->
                    lsVm.currentDrawerScreen = newScreen
                },
                goToSettingsScreen = {
                    navController.navigate(HarvestRoutes.Screen.SETTINGS)
                },
                orgName = userOrganization?.name,
                userState = userState
            )
        },
        drawerBackgroundColor = DrawerBgColor,
        scaffoldState = scaffoldState,
        backgroundColor = SurfaceColor
    ) { bodyPadding ->
        AnimatedContent(
            targetState = lsVm.currentDrawerScreen,
            transitionSpec = { fadeIn() + fadeIn() with fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(bodyPadding)
        ) { drawerScreenState ->
            when (drawerScreenState) {
                LandingScreenDrawerItemType.Time -> TimeScreen(
                    pagerState = timeScreenPagerState,
                    startIndex = lsVm.timeScreenStartIndex,
                    onWeekScrolled = { weekOffset ->
                        lsVm.currentDayOffset += weekOffset.times(WeekDays.values().size)
                    },
                    onDayScrolled = { dayOffset ->
                        lsVm.currentDayOffset = dayOffset
                    },
                    goToNewEntryScreen = { workRequest, requestType ->
                        nesVm.updateCurrentWorkRequestType(
                            workRequestType = requestType,
                            onUpdateCompleted = {
                                nesVm.updateCurrentWorkRequest(
                                    update = { workRequest },
                                    onUpdateCompleted = {
                                        navController.navigate(HarvestRoutes.Screen.WORK_ENTRY)
                                    })
                            }
                        )
                    },
                    isWorkLoading = { isLoading ->
                        lsVm.isWorkLoading = isLoading
                    },
                    navigateToFindWorkspaceScreen = { navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE },
                    localWeekOffset = lsVm.localWeekOffset,
                    onWeekOffsetChanged = { updatedOffset ->
                        lsVm.localWeekOffset += updatedOffset
                    },
                    onUpdateWeekLogsTotalTime = { updatedTime ->
                        lsVm.currentWeekLogsTotalTime = updatedTime
                    },
                    userOrgName = maVm.userOrganization?.name,
                    getUserState = userState
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