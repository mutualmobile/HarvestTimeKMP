package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.common.noAccountAnnotatedString
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SignInTextField
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.SurfaceTextButton
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.android.ui.utils.get
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.LoginDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginScreen(
    navController: NavHostController,
    userState: DataState,
    onLoginSuccess: () -> Unit
) {
    var currentWorkEmail by remember { mutableStateOf("anmol.verma4@gmail.com") }
    var currentPassword by remember { mutableStateOf("password") }

    var currentNavigationCommand: PraxisCommand? by remember { mutableStateOf(null) }

    var currentLoginState: DataState by remember {
        mutableStateOf(EmptyState)
    }
    val loginDataModel by remember {
        mutableStateOf(
            LoginDataModel().apply {
                this.dataFlow.onEach { loginState ->
                    currentLoginState = loginState
                }.launchIn(this.dataModelScope)
                praxisCommand.onEach {  newCommand ->
                    currentNavigationCommand = newCommand
                    when (currentNavigationCommand) {
                        is NavigationPraxisCommand -> {
                            onLoginSuccess()
                        }
                    } }.launchIn(dataModelScope)
            }
        )
    }

    var currentErrorMsg: String? by remember { mutableStateOf(null) }

    LaunchedEffect(userState) {
        if (userState is PraxisDataModel.SuccessState<*> && currentNavigationCommand is NavigationPraxisCommand) {
            navController clearBackStackAndNavigateTo (currentNavigationCommand as NavigationPraxisCommand).screen
        }
    }

    LaunchedEffect(currentLoginState) {
        currentErrorMsg = when (currentLoginState) {
            is ErrorState -> (currentLoginState as ErrorState).throwable.message
            else -> null
        }
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
            SignInTextField(
                value = currentWorkEmail,
                onValueChange = { updatedString -> currentWorkEmail = updatedString },
                placeholderText = stringResource(MR.strings.login_screen_email_et_placeholder.resourceId)
            )
            SignInTextField(
                value = currentPassword,
                onValueChange = { updatedString -> currentPassword = updatedString },
                placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId),
                isPasswordTextField = true
            )
            SurfaceTextButton(
                text = stringResource(MR.strings.forgot_password.resourceId),
                fontWeight = FontWeight.Medium,
                onClick = { navController.navigate(HarvestRoutes.Screen.FORGOT_PASSWORD) }
            )
            IconLabelButton(
                label = stringResource(MR.strings.login_screen_signIn_btn_txt.resourceId),
                onClick = { loginDataModel.login(currentWorkEmail.trim(), currentPassword.trim()) },
                isLoading = currentLoginState is LoadingState || userState is LoadingState,
                errorMsg = currentErrorMsg,
            )
            SurfaceTextButton(
                text = noAccountAnnotatedString(),
                onClick = { navController.navigate(HarvestRoutes.Screen.SIGNUP) }
            )
            SurfaceTextButton(
                text = MR.strings.view_tour.get(),
                fontWeight = FontWeight.Medium,
                onClick = {
                    navController clearBackStackAndNavigateTo HarvestRoutes.Screen.ON_BOARDING
                }
            )
        }
        HarvestDialog(
            praxisCommand = currentNavigationCommand,
            onConfirm = {
                currentNavigationCommand = null
            },
        )
    }
}