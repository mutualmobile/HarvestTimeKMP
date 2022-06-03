package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.SignUpDataModel

@Composable
fun NewOrgSignUpScreen(navController: NavHostController) {
    var signUpState: DataState by remember { mutableStateOf(EmptyState) }
    val signUpDataModel by remember { mutableStateOf(SignUpDataModel { updatedState ->
        signUpState = updatedState
    }) }
    var currentWorkEmail by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var currentFirstName by remember { mutableStateOf("") }
    var currentLastName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyWebsite by remember { mutableStateOf("") }
    var companyIdentifier by remember { mutableStateOf("") }
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
                value = companyName,
                onValueChange = { updatedString -> companyName = updatedString },
                placeholderText = stringResource(MR.strings.signup_screen_company_name_et_placeholder.resourceId)
            )
            SignUpTextField(
                value = companyWebsite,
                onValueChange = { updatedString -> companyWebsite = updatedString },
                placeholderText = stringResource(MR.strings.signup_screen_company_website_et_placeholder.resourceId)
            )
            SignUpTextField(
                value = companyIdentifier,
                onValueChange = { updatedString -> companyIdentifier = updatedString },
                placeholderText = stringResource(MR.strings.signup_screen_company_identifier_et_placeholder.resourceId)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = stringResource(MR.strings.signup_screen_user_details_et_placeholder.resourceId))
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
                placeholderText = stringResource(MR.strings.signup_screen_password_et_placeholder.resourceId),
                isPasswordTextField = true
            )
            IconLabelButton(
                label = stringResource(MR.strings.signup_screen_signup_btn_txt.resourceId),
                onClick = {
                    signUpDataModel.signUp(
                        currentFirstName,
                        currentLastName,
                        currentWorkEmail,
                        currentPassword,
                        companyName,
                        companyWebsite,
                        companyIdentifier
                    )
                }
            )
        }
    }
}