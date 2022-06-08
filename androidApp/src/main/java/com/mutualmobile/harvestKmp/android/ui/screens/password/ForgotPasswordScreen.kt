package com.mutualmobile.harvestKmp.android.ui.screens.password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.ScreenList
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.android.ui.utils.navigateAndClear
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(MR.strings.forgot_password.resourceId),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                contentPadding = WindowInsets.Companion.statusBars.asPaddingValues(),

                backgroundColor = MaterialTheme.colors.primary,

                )

        },
    ) { bodyPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .systemBarsPadding()
                .padding(bodyPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SignUpTextField(
                    value = currentWorkEmail,
                    onValueChange = { updatedString -> currentWorkEmail = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
                )

                IconLabelButton(
                    errorMsg = (forgotPasswordState as? ErrorState)?.throwable?.message,
                    label = stringResource(MR.strings.request_reset_password.resourceId),
                    onClick = {
                        forgotPasswordDataModel.forgotPassword(
                            currentWorkEmail.trim()
                        )
                    }
                )
            }
        }
    }
}