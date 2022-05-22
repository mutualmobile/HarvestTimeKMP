package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.OrDivider
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SignInTextField
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SurfaceTextButton
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel

@Composable
fun LoginScreen(
    initiateGoogleSignIn: () -> Unit,
    // TODO: Change this
    initiateEmailPasswordSignIn: () -> Unit = initiateGoogleSignIn,
    loginDataModel: LoginDataModel
) {
    var currentWorkEmail by remember { mutableStateOf("anmol.verma4@gmail.com") }
    var currentPassword by remember { mutableStateOf("password") }

    val activity = LocalContext.current as Activity

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
            IconLabelButton(
                icon = R.drawable.google_logo,
                label = stringResource(R.string.login_screen_google_btn_txt),
                onClick = initiateGoogleSignIn
            )
            OrDivider()
            SignInTextField(
                value = currentWorkEmail,
                onValueChange = { updatedString -> currentWorkEmail = updatedString },
                placeholderText = stringResource(R.string.login_screen_email_et_placeholder)
            )
            SignInTextField(
                value = currentPassword,
                onValueChange = { updatedString -> currentPassword = updatedString },
                placeholderText = stringResource(R.string.login_screen_password_et_placeholder),
                isPasswordTextField = true
            )
            IconLabelButton(
                label = stringResource(R.string.login_screen_signIn_btn_txt),
                onClick = {
//                    initiateEmailPasswordSignIn
                    loginDataModel.login(currentWorkEmail, currentPassword)
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

