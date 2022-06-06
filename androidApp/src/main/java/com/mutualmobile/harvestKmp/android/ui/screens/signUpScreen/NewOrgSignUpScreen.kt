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
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.features.harvest.SignUpDataModel

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
                SignUpTextField(
                    value = currentFirstName,
                    onValueChange = { updatedString -> currentFirstName = updatedString },
                    hintText = stringResource(MR.strings.signup_screen_first_name_et_placeholder.resourceId),
                )
                SignUpTextField(
                    value = currentLastName,
                    onValueChange = { updatedString -> currentLastName = updatedString },
                    hintText = stringResource(MR.strings.signup_screen_last_name_et_placeholder.resourceId)
                )

                SignUpTextField(
                    value = currentWorkEmail,
                    onValueChange = { updatedString -> currentWorkEmail = updatedString },
                    hintText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = currentPassword,
                    onValueChange = { updatedString -> currentPassword = updatedString },
                    hintText = stringResource(MR.strings.password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                SignUpTextField(
                    value = confirmPassword,
                    onValueChange = { updatedString -> confirmPassword = updatedString },
                    hintText = stringResource(MR.strings.signup_screen_confirm_password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                SignUpTextField(
                    value = companyName,
                    onValueChange = { updatedString -> companyName = updatedString },
                    hintText = stringResource(MR.strings.signup_screen_company_name_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = companyWebsite,
                    onValueChange = { updatedString -> companyWebsite = updatedString },
                    hintText = stringResource(MR.strings.signup_screen_company_website_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = companyIdentifier,
                    onValueChange = { updatedString -> companyIdentifier = updatedString },
                    hintText = stringResource(MR.strings.signup_screen_company_identifier_et_placeholder.resourceId)
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