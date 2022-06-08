package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.OrDivider
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SignInTextField
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SurfaceTextButton
import com.mutualmobile.harvestKmp.android.ui.utils.navigateAndClear
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.LoginDataModel

@Composable
fun LoginScreen(navController: NavHostController) {
    var currentWorkEmail by remember { mutableStateOf("anmol.verma4@gmail.com") }
    var currentPassword by remember { mutableStateOf("password") }

    var currentLoginState: DataState by remember {
        mutableStateOf(EmptyState)
    }

    val loginDataModel by remember {
        mutableStateOf(
            LoginDataModel { loginState ->
                currentLoginState = loginState
                when (loginState) {
                    is SuccessState<*> -> {
                        navController.navigateAndClear(
                            clearRoute = ScreenList.LoginScreen(),
                            navigateTo = ScreenList.LandingScreen()
                        )
                    }
                    else -> Unit
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .systemBarsPadding()
            .padding(top = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconLabelButton(
                icon = R.drawable.google_logo,
                label = stringResource(MR.strings.login_screen_google_btn_txt.resourceId),
                isLoading = currentLoginState is LoadingState,
                errorMsg = (currentLoginState as? ErrorState)?.throwable?.message,
                onClick = {
                    loginDataModel.login(currentWorkEmail.trim(), currentPassword.trim())
                }
            )
            OrDivider()
            SignInTextField(
                value = currentWorkEmail,
                onValueChange = { updatedString -> currentWorkEmail = updatedString },
                placeholderText = stringResource(MR.strings.login_screen_email_et_placeholder.resourceId)
            )
            SignInTextField(
                value = currentPassword,
                onValueChange = { updatedString -> currentPassword = updatedString },
                placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId),
                isPasswordTextField = true
            )
            SurfaceTextButton(
                text = stringResource(MR.strings.forgot_password.resourceId),
                fontWeight = FontWeight.Medium,
                onClick = { navController.navigate(ScreenList.ForgotPasswordScreen()) }
            )
            IconLabelButton(
                label = stringResource(MR.strings.login_screen_signIn_btn_txt.resourceId),
                onClick = { loginDataModel.login(currentWorkEmail.trim(), currentPassword.trim()) }
            )
            SurfaceTextButton(
                text = buildAnnotatedString {
                    append("Don't have an account?")
                    withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                        append(" Try Harvest Free")
                    }
                },
                onClick = { navController.navigate(ScreenList.ExistingOrgSignUpScreen()) }
            )
            SurfaceTextButton(
                text = "View Tour",
                fontWeight = FontWeight.Medium,
                onClick = navController::navigateUp
            )
        }
    }
}

