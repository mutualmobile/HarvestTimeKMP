package com.mutualmobile.harvestKmp.android.ui.screens.password

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels.ForgotPasswordDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    var forgotPasswordState: DataState by remember { mutableStateOf(EmptyState) }

    var forgotPasswordNavigationCommand: PraxisCommand? by remember { mutableStateOf(null) }
    val forgotPasswordDataModel by remember {
        mutableStateOf(
            ForgotPasswordDataModel().apply {
                this.dataFlow.onEach { passwordState ->
                    forgotPasswordState = passwordState
                }.launchIn(coroutineScope)
                praxisCommand.onEach {  newCommand ->
                    forgotPasswordNavigationCommand = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            if (newCommand.screen.isBlank()) {
                                navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE
                            }
                        }
                    } }.launchIn(coroutineScope)
            }
        )
    }
    var currentWorkEmail by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(MR.strings.forgot_password.resourceId),
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
                .padding(bodyPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SignUpTextField(
                    value = currentWorkEmail,
                    onValueChange = { updatedString -> currentWorkEmail = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_email_et_placeholder.resourceId)
                )

                IconLabelButton(
                    errorMsg = (forgotPasswordState as? ErrorState)?.throwable?.message,
                    label = stringResource(MR.strings.request_reset_password.resourceId),
                    onClick = {
                        forgotPasswordDataModel.forgotPassword(
                            currentWorkEmail.trim()
                        )
                    }
                )
            }
            HarvestDialog(praxisCommand = forgotPasswordNavigationCommand, onConfirm = {
                forgotPasswordNavigationCommand = null
                when (forgotPasswordState) {
                    is SuccessState<*> -> {
                        navController.navigateUp()
                    }
                    else -> Unit
                }
            })
        }
    }
}