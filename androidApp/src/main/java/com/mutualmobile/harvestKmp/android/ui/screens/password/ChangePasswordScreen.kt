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
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.ChangePasswordDataModel
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(navController: NavHostController) {
    var changePasswordState: DataState by remember { mutableStateOf(EmptyState) }
    val scope = rememberCoroutineScope()

    val changePasswordDataModel by remember {
        mutableStateOf(
            ChangePasswordDataModel { passwordState ->
                changePasswordState = passwordState
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
    var currentPassword by remember { mutableStateOf("") }
    var currentConfirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(MR.strings.change_password.resourceId),
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
                .padding(bodyPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SignUpTextField(
                    value = currentPassword,
                    onValueChange = { updatedString -> currentPassword = updatedString },
                    placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = currentConfirmPassword,
                    onValueChange = { updatedString -> currentConfirmPassword = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_confirm_password_et_placeholder.resourceId)
                )

                IconLabelButton(
                    label = stringResource(MR.strings.request_reset_password.resourceId),
                    errorMsg = (changePasswordState as? ErrorState)?.throwable?.message
                ) {
                    scope.launch {
                        changePasswordDataModel.changePassWord(
                            currentPassword.trim(),
                            currentConfirmPassword.trim()
                        ).collect(
                        )
                    }


                }
            }
        }
    }
}