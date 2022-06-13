package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.android.viewmodels.ExistingOrgSignUpScreenViewModel
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import org.koin.androidx.compose.get

@Composable
fun SignUpScreen(
    navController: NavHostController,
    eossVm: ExistingOrgSignUpScreenViewModel = get()
) {
    LaunchedEffect(eossVm.signUpPraxisCommand) {
        when (eossVm.signUpPraxisCommand) {
            is NavigationPraxisCommand -> {
                val destination = (eossVm.signUpPraxisCommand as NavigationPraxisCommand).screen
                eossVm.resetAll {
                    navController clearBackStackAndNavigateTo destination
                }
            }
        }
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
                contentPadding = WindowInsets.statusBars.asPaddingValues(),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
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
                    value = eossVm.currentFirstName,
                    onValueChange = { updatedString -> eossVm.currentFirstName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_first_name_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = eossVm.currentLastName,
                    onValueChange = { updatedString -> eossVm.currentLastName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_last_name_et_placeholder.resourceId)
                )

                SignUpTextField(
                    value = eossVm.currentWorkEmail,
                    onValueChange = { updatedString -> eossVm.currentWorkEmail = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = eossVm.currentPassword,
                    onValueChange = { updatedString -> eossVm.currentPassword = updatedString },
                    placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                SignUpTextField(
                    value = eossVm.currentConfirmPassword,
                    onValueChange = { updatedString ->
                        eossVm.currentConfirmPassword = updatedString
                    },
                    placeholderText = stringResource(MR.strings.signup_screen_confirm_password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                IconLabelButton(
                    label = stringResource(MR.strings.signup_screen_signup_btn_txt.resourceId),
                    isLoading = eossVm.currentSignUpState is LoadingState,
                    errorMsg = (eossVm.currentSignUpState as? ErrorState)?.throwable?.message,
                    onClick = { eossVm.signUp() }
                )
            }
        }
        HarvestDialog(
            praxisCommand = eossVm.signUpPraxisCommand,
            onConfirm = {
                eossVm.signUpPraxisCommand = null
            },
        )
    }
}