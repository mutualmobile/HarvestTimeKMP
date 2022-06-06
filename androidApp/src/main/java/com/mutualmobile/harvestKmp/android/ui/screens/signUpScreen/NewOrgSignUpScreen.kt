package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.NewSignUpTextField
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.SignUpDataModel

@Composable
fun NewOrgSignUpScreen(navController: NavHostController) {
    var signUpState: DataState by remember { mutableStateOf(EmptyState) }
    val signUpDataModel by remember {
        mutableStateOf(SignUpDataModel { updatedState ->
            signUpState = updatedState
        })
    }
    var currentWorkEmail by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentFirstName by remember { mutableStateOf("") }
    var currentLastName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyWebsite by remember { mutableStateOf("") }
    var companyIdentifier by remember { mutableStateOf("") }

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
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NewSignUpTextField(
                    value = currentFirstName,
                    onValueChange = { updatedString -> currentFirstName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_first_name_et_placeholder.resourceId),
                )
                NewSignUpTextField(
                    value = currentLastName,
                    onValueChange = { updatedString -> currentLastName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_last_name_et_placeholder.resourceId)
                )

                NewSignUpTextField(
                    value = currentWorkEmail,
                    onValueChange = { updatedString -> currentWorkEmail = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
                )
                NewSignUpTextField(
                    value = currentPassword,
                    onValueChange = { updatedString -> currentPassword = updatedString },
                    placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                NewSignUpTextField(
                    value = confirmPassword,
                    onValueChange = { updatedString -> confirmPassword = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_confirm_password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                NewSignUpTextField(
                    value = companyName,
                    onValueChange = { updatedString -> companyName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_company_name_et_placeholder.resourceId)
                )
                NewSignUpTextField(
                    value = companyWebsite,
                    onValueChange = { updatedString -> companyWebsite = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_company_website_et_placeholder.resourceId)
                )
                NewSignUpTextField(
                    value = companyIdentifier,
                    onValueChange = { updatedString -> companyIdentifier = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_company_identifier_et_placeholder.resourceId)
                )
                IconLabelButton(
                    modifier = Modifier.padding(
                        top = 12.dp
                    ),
                    label = stringResource(MR.strings.signup_screen_signup_btn_txt.resourceId),
                    onClick = {
                        signUpDataModel.signUp(
                            currentFirstName,
                            currentLastName,
                            currentWorkEmail,
                            currentPassword,
                            confirmPassword,
                            companyName,
                            companyWebsite,
                            companyIdentifier
                        )
                    }
                )
            }
        }
    }
}