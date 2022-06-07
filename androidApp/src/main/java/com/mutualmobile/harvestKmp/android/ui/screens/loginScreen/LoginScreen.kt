package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SignInTextField
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SurfaceTextButton
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.features.harvest.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.LoginDataModel

@Composable
fun LoginScreen(navController: NavHostController, orgIdentifier: String?) {
    var currentWorkEmail by remember { mutableStateOf("anmol@mutualmobile.com") }
    var currentPassword by remember { mutableStateOf("password") }

    var userState: DataState by remember { mutableStateOf(EmptyState) }
    val userDataModel by remember {
        mutableStateOf(
            GetUserDataModel { newState ->
                userState = newState
            }
        )
    }

    var currentLoginState: DataState by remember {
        mutableStateOf(EmptyState)
    }
    val loginDataModel by remember {
        mutableStateOf(
            LoginDataModel { loginState ->
                currentLoginState = loginState
                when (loginState) {
                    is SuccessState<*> -> {
                        userDataModel.activate()
                    }
                    else -> Unit
                }
            }
        )
    }

    var currentErrorMsg: String? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = currentLoginState, key2 = userState) {
        currentErrorMsg = when {
            currentLoginState is ErrorState -> (currentLoginState as ErrorState).throwable.message
            userState is ErrorState -> (userState as ErrorState).throwable.message
            else -> null
        }
    }

    LaunchedEffect(userState) {
        if (userState is SuccessState<*>) {
            orgIdentifier?.let { nnOrgIdentifier ->
                println("Identifier is: $nnOrgIdentifier")
                navController clearBackStackAndNavigateTo ScreenList.LandingScreen.orgIdentifierRoute(
                    orgIdentifier = nnOrgIdentifier
                )
            }
        }
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
            SignInTextField(
                value = currentWorkEmail,
                onValueChange = { updatedString -> currentWorkEmail = updatedString },
                placeholderText = stringResource(MR.strings.login_screen_email_et_placeholder.resourceId)
            )
            SignInTextField(
                value = currentPassword,
                onValueChange = { updatedString -> currentPassword = updatedString },
                placeholderText = stringResource(MR.strings.login_screen_password_et_placeholder.resourceId),
                isPasswordTextField = true
            )
            IconLabelButton(
                label = stringResource(MR.strings.login_screen_signIn_btn_txt.resourceId),
                onClick = { loginDataModel.login(currentWorkEmail.trim(), currentPassword.trim()) },
                isLoading = currentLoginState is LoadingState || userState is LoadingState,
                errorMsg = currentErrorMsg,
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
                onClick = {
                    navController clearBackStackAndNavigateTo ScreenList.OnBoardingScreen()
                }
            )
        }
    }
}

