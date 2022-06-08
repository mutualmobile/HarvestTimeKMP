package com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.android.ui.utils.navigateAndClear
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels.ForgotPasswordDataModel

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var forgotPasswordState: DataState by remember { mutableStateOf(EmptyState) }

    val forgotPasswordDataModel by remember {
        mutableStateOf(
            ForgotPasswordDataModel { passwordState ->
                forgotPasswordState = passwordState
                when (passwordState) {
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
    var currentWorkEmail by remember { mutableStateOf("") }

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
            Text(text = stringResource(MR.strings.signup_screen_company_details_et_placeholder.resourceId))

            SignUpTextField(
                value = currentWorkEmail,
                onValueChange = { updatedString -> currentWorkEmail = updatedString },
                placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
            )

            IconLabelButton(
                label = stringResource(MR.strings.request_reset_password.resourceId),
                onClick = {
                    forgotPasswordDataModel.forgotPassword(
                        currentWorkEmail
                    )
                }
            )
        }
    }
}