package com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SurfaceTextButton
import com.mutualmobile.harvestKmp.android.ui.utils.navigateAndClear
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.OnBoardingDataModel
import java.util.*
import com.mutualmobile.harvestKmp.MR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    var currentbBoardingState: DataState by remember {
        mutableStateOf(EmptyState)
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val onBoardingDataModel by remember {
        mutableStateOf(
            OnBoardingDataModel { signUpState ->
                currentbBoardingState = signUpState
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


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) { bodyPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bodyPadding)
        ) {
            LaunchedEffect(key1 = Unit) {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(onBoardingDataModel.getOnBoardingItemList()[pagerState.currentPage].color))

                ) {

                HorizontalPager(
                    state = pagerState,
                    count = onBoardingDataModel.getOnBoardingItemList().size
                ) { page ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "OnBoardImage",
                                modifier = Modifier
                                    .size(100.dp)
                            )
                            Text(
                                text = stringResource(MR.strings.app_name.resourceId).lowercase(
                                    Locale.getDefault()
                                ),
                                color = Color.White,
                                fontSize = 30.sp
                            )

                        }


                        Text(
                            text = onBoardingDataModel.getOnBoardingItemList()[page].title,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                    }
                }



                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(250.dp)
                        .fillMaxWidth()
                        .background(Color(onBoardingDataModel.getOnBoardingItemList()[pagerState.currentPage].colorBottom))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .fillMaxHeight(),
                        verticalArrangement = if (pagerState.currentPage != 2) {
                            Arrangement.Bottom
                        } else {
                            Arrangement.Bottom
                        },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        PagerIndicator(
                            onBoardingDataModel.getOnBoardingItemList().size,
                            pagerState.currentPage
                        )

                        IconLabelButton(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .padding(10.dp),
                            label = stringResource(MR.strings.login_screen_signIn_btn_txt.resourceId),
                            isLoading = currentbBoardingState is LoadingState,
                            errorMsg = (currentbBoardingState as? ErrorState)?.throwable?.message,
                            onClick = {
                                navController.navigateAndClear(
                                    clearRoute = ScreenList.OnBoardingScreen (),
                                    navigateTo = ScreenList.LoginScreen()
                                )
                            }
                        )
                        SurfaceTextButton(
                            text = buildAnnotatedString {
                                append("Don't have an account?")
                                withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                                    append(" Try Harvest Free")
                                }
                            }.toString()
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun PagerIndicator(size: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        repeat(size) {
            IndicateIcon(
                isSelected = it == currentPage
            )
        }
    }
}


@Composable
fun IndicateIcon(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(10.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) {
                    Color.White
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                }
            )
    )
}