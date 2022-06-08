package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.SignUpDataModel

@Composable
fun SignUpScreen(navController: NavHostController) {
    var currentWorkEmail by remember { mutableStateOf("test@gmail.com") }
    var currentPassword by remember { mutableStateOf("testpassword") }
    var currentFirstName by remember { mutableStateOf("test") }
    var currentLastName by remember { mutableStateOf("test") }
    var currentCompanyName by remember { mutableStateOf("5186f350-1f0e-42b7-b07e-ab36eb460552") }

    var currentSignUpState: DataState by remember {
        mutableStateOf(EmptyState)
    }

    var signUpPraxisCommand: PraxisCommand? by remember { mutableStateOf(null) }
    val signUpDataModel by remember {
        mutableStateOf(
            SignUpDataModel { signUpState ->
                currentSignUpState = signUpState
            }.apply {
                praxisCommand = { newCommand ->
                    signUpPraxisCommand = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            navController clearBackStackAndNavigateTo newCommand.screen
                        }
                    }
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
                value = currentCompanyName,
                onValueChange = { updatedString -> currentCompanyName = updatedString },
                placeholderText = stringResource(MR.strings.signup_screen_company_name_et_placeholder.resourceId)
            )
            SignUpTextField(
                value = currentWorkEmail,
                onValueChange = { updatedString -> currentWorkEmail = updatedString },
                placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
            )
            SignUpTextField(
                value = currentPassword,
                onValueChange = { updatedString -> currentPassword = updatedString },
                placeholderText = stringResource(MR.strings.signup_screen_password_et_placeholder.resourceId),
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
                        company = currentCompanyName,
                        email = currentWorkEmail,
                        password = currentPassword
                    )
                }
            )
        }
        HarvestDialog(
            praxisCommand = signUpPraxisCommand,
            onConfirm = {
                signUpPraxisCommand = null
            },
        )
    }
}