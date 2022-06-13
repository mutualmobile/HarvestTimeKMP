package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.android.viewmodels.NewOrgSignUpScreenViewModel
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import org.koin.androidx.compose.get

@Composable
fun NewOrgSignUpScreen(
    navController: NavHostController,
    nossVm: NewOrgSignUpScreenViewModel = get()
) {
    LaunchedEffect(nossVm.currentPraxisCommand) {
        when (nossVm.currentPraxisCommand) {
            is NavigationPraxisCommand -> {
                val destination = (nossVm.currentPraxisCommand as NavigationPraxisCommand).screen
                nossVm.resetAll {
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
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
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
                .navigationBarsPadding()
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
                    value = nossVm.currentFirstName,
                    onValueChange = { updatedString -> nossVm.currentFirstName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_first_name_et_placeholder.resourceId),
                )
                SignUpTextField(
                    value = nossVm.currentLastName,
                    onValueChange = { updatedString -> nossVm.currentLastName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_last_name_et_placeholder.resourceId)
                )

                SignUpTextField(
                    value = nossVm.currentWorkEmail,
                    onValueChange = { updatedString -> nossVm.currentWorkEmail = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = nossVm.currentPassword,
                    onValueChange = { updatedString -> nossVm.currentPassword = updatedString },
                    placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                SignUpTextField(
                    value = nossVm.confirmPassword,
                    onValueChange = { updatedString -> nossVm.confirmPassword = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_confirm_password_et_placeholder.resourceId),
                    isPasswordTextField = true
                )
                SignUpTextField(
                    value = nossVm.companyName,
                    onValueChange = { updatedString -> nossVm.companyName = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_company_name_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = nossVm.companyWebsite,
                    onValueChange = { updatedString -> nossVm.companyWebsite = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_company_website_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = nossVm.companyIdentifier,
                    onValueChange = { updatedString -> nossVm.companyIdentifier = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_company_identifier_et_placeholder.resourceId)
                )
                IconLabelButton(
                    modifier = Modifier.padding(
                        top = 12.dp
                    ),
                    label = stringResource(MR.strings.signup_screen_signup_btn_txt.resourceId),
                    onClick = {
                        nossVm.signUpDataModel.signUp(
                            firstName = nossVm.currentFirstName,
                            lastName = nossVm.currentLastName,
                            email = nossVm.currentWorkEmail,
                            password = nossVm.currentPassword,
                            orgName = nossVm.companyName,
                            orgWebsite = nossVm.companyWebsite,
                            orgIdentifier = nossVm.companyIdentifier
                        )
                    },
                    isLoading = nossVm.signUpState is LoadingState,
                    errorMsg = (nossVm.signUpState as? ErrorState)?.throwable?.message
                )
            }
            HarvestDialog(praxisCommand = nossVm.currentPraxisCommand, onConfirm = {
                nossVm.currentPraxisCommand = null
            })
        }
    }
}