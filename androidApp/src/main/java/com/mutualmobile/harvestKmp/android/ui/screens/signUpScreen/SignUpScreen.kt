package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen

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
import com.google.accompanist.insets.systemBarsPadding
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SignInTextField
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SurfaceTextButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.features.harvest.SignUpDataModel

@Composable
fun SignUpScreen(
    signUpDataModel: SignUpDataModel
) {
    var currentWorkEmail by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var currentFirstName by remember { mutableStateOf("") }
    var currentLastName by remember { mutableStateOf("") }
    var currentCompanyName by remember { mutableStateOf("5186f350-1f0e-42b7-b07e-ab36eb460552") }
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
                placeholderText = stringResource(R.string.signup_screen_first_name_et_placeholder)
            )
            SignUpTextField(
                value = currentLastName,
                onValueChange = { updatedString -> currentLastName = updatedString },
                placeholderText = stringResource(R.string.signup_screen_last_name_et_placeholder)
            )
            SignUpTextField(
                value = currentCompanyName,
                onValueChange = { updatedString -> currentCompanyName = updatedString },
                placeholderText = stringResource(R.string.signup_screen_company_name_et_placeholder)
            )
            SignUpTextField(
                value = currentWorkEmail,
                onValueChange = { updatedString -> currentWorkEmail = updatedString },
                placeholderText = stringResource(R.string.signup_screen_email_et_placeholder)
            )
            SignUpTextField(
                value = currentPassword,
                onValueChange = { updatedString -> currentPassword = updatedString },
                placeholderText = stringResource(R.string.signup_screen_password_et_placeholder),
                isPasswordTextField = true
            )
            IconLabelButton(
                label = stringResource(R.string.signup_screen_signup_btn_txt),
                onClick = {
                    signUpDataModel.signUp(
                        currentFirstName,
                        currentLastName,
                        currentCompanyName,
                        currentWorkEmail,
                        currentPassword
                    )
                }
            )
            SurfaceTextButton(
                text = buildAnnotatedString {
                    append("Don't have an account?")
                    withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                        append(" Try Harvest Free")
                    }
                }.toString()
            )
            SurfaceTextButton(text = "View Tour", fontWeight = FontWeight.Medium)
        }
    }
}