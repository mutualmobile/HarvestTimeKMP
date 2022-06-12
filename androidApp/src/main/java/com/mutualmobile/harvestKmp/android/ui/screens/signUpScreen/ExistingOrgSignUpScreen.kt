package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.SignUpDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SignUpScreen(navController: NavHostController) {
    var currentWorkEmail by remember { mutableStateOf("test@gmail.com") }
    var currentPassword by remember { mutableStateOf("testpassword") }
    var currentConfirmPassword by remember { mutableStateOf("testpassword") }
    var currentFirstName by remember { mutableStateOf("test") }
    var currentLastName by remember { mutableStateOf("test") }

    var currentSignUpState: DataState by remember {
        mutableStateOf(EmptyState)
    }

    var signUpPraxisCommand: PraxisCommand? by remember { mutableStateOf(null) }
    val signUpDataModel by remember {
        mutableStateOf(
            SignUpDataModel().apply {
                this.dataFlow.onEach {  signUpState ->
                    currentSignUpState = signUpState }.launchIn(this.dataModelScope)
                praxisCommand.onEach { newCommand ->
                    signUpPraxisCommand = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            navController clearBackStackAndNavigateTo newCommand.screen
                        }
                    } }.launchIn(dataModelScope)
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(MR.strings.sign_up_form.resourceId),
                    )
                },
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
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignUpTextField(
                    value = currentFirstName,
                    onValueChange = { updatedString -> currentFirstName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_first_name_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = currentLastName,
                    onValueChange = { updatedString -> currentLastName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_last_name_et_placeholder.resourceId)
                )

                SignUpTextField(
                    value = currentWorkEmail,
                    onValueChange = { updatedString -> currentWorkEmail = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = currentPassword,
                    onValueChange = { updatedString -> currentPassword = updatedString },
                    placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                SignUpTextField(
                    value = currentConfirmPassword,
                    onValueChange = { updatedString -> currentConfirmPassword = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_confirm_password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                IconLabelButton(
                    label = stringResource(MR.strings.signup_screen_signup_btn_txt.resourceId),
                    isLoading = currentSignUpState is LoadingState,
                    errorMsg = (currentSignUpState as? ErrorState)?.throwable?.message,
                    onClick = {
                        signUpDataModel.signUp(
                            firstName = currentFirstName,
                            lastName = currentLastName,
                            company = currentConfirmPassword,
                            email = currentWorkEmail,
                            password = currentPassword
                        )
                    }
                )
            }
        }
        HarvestDialog(
            praxisCommand = signUpPraxisCommand,
            onConfirm = {
                signUpPraxisCommand = null
            },
        )
    }
}