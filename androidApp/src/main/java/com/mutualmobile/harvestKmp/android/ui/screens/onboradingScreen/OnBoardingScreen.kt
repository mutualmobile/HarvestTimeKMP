package com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SurfaceTextButton
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme
import com.mutualmobile.harvestKmp.android.ui.utils.navigateAndClear
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.features.datamodels.OnBoardingDataModel

val OnBoardingImages = listOf(
    R.drawable.onboarding_screen_1,
    R.drawable.onboarding_screen_2,
    R.drawable.onboarding_screen_3,
    R.drawable.onboarding_screen_4,
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    var currentBoardingState: DataState by remember {
        mutableStateOf(EmptyState)
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val onBoardingDataModel by remember {
        mutableStateOf(
            OnBoardingDataModel { signUpState ->
                currentBoardingState = signUpState
                when (signUpState) {
                    is SuccessState<*> -> {
                        navController.navigateAndClear(
                            clearRoute = ScreenList.ExistingOrgSignUpScreen(),
                            navigateTo = ScreenList.LandingScreen(),
                        )
                    }
                    else -> Unit
                }
            }
        )
    }

    val color by animateColorAsState(
        targetValue = Color(
            onBoardingDataModel.getOnBoardingItemList()[pagerState.currentPage].color
        ),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
    ) { bodyPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bodyPadding),
            color = color
        ) {
            LaunchedEffect(key1 = Unit) {
                pagerState.animateScrollToPage(page = pagerState.currentPage)
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(MR.strings.app_name.resourceId).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .statusBarsPadding()
                            .padding(top = 24.dp, bottom = 8.dp)
                    )

                    ScrollingText(
                        onBoardingDataModel = onBoardingDataModel,
                        pagerState = pagerState
                    )
                }

                Column {
                    HorizontalPager(
                        state = pagerState,
                        count = onBoardingDataModel.getOnBoardingItemList().size,
                        verticalAlignment = Alignment.Bottom,
                    ) { page ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.425f),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Image(
                                painter = painterResource(id = OnBoardingImages[page]),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.navigationBarsPadding(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            HorizontalPagerIndicator(
                                pagerState = pagerState,
                                activeColor = Color.White
                            )

                            IconLabelButton(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .padding(top = 16.dp),
                                label = stringResource(MR.strings.login_screen_signIn_btn_txt.resourceId),
                                isLoading = currentBoardingState is LoadingState,
                                errorMsg = (currentBoardingState as? ErrorState)?.throwable?.message,
                                onClick = { navController.navigate(ScreenList.FindWorkspaceScreen()) }
                            )

                            SurfaceTextButton(
                                text = buildAnnotatedString {
                                    append("Don't have an account?")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(" Try Harvest Free")
                                    }
                                },
                                onClick = { navController.navigate(ScreenList.ExistingOrgSignUpScreen()) }
                            )

                            Spacer(modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalPagerApi::class)
private fun ScrollingText(
    onBoardingDataModel: OnBoardingDataModel,
    pagerState: PagerState
) {
    val textPagerState = rememberPagerState()
    VerticalPager(
        count = onBoardingDataModel.getOnBoardingItemList().size,
        state = textPagerState,
        modifier = Modifier.height(50.dp),
        userScrollEnabled = false
    ) { index ->
        Text(
            text = onBoardingDataModel.getOnBoardingItemList()[index].title,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }

    LaunchedEffect(pagerState.currentPage) {
        textPagerState.animateScrollToPage(pagerState.currentPage)
    }
}

@Preview
@Composable
fun OnBoardingScreenPreview() {
    HarvestKmpTheme {
        OnBoardingScreen(navController = rememberNavController())
    }
}