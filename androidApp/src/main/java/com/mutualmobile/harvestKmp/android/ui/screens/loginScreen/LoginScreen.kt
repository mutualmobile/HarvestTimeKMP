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
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.withOrgId
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.LoginDataModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    orgIdentifier: String?,
) {
    var currentWorkEmail by remember { mutableStateOf("anmol.verma4@gmail.com") }
    var currentPassword by remember { mutableStateOf("password") }

    var currentNavigationCommand: PraxisCommand? by remember { mutableStateOf(null) }

    var userState: DataState by remember { mutableStateOf(EmptyState) }
    val userDataModel by remember {
        mutableStateOf(
            GetUserDataModel { newState ->
                userState = newState
            }.apply {
                praxisCommand = { newCommand ->
                    currentNavigationCommand = newCommand
                }
            }
        )
    }

    var currentLoginState: DataState by remember {
        mutableStateOf(EmptyState)
    }
    val loginDataModel by remember {
        mutableStateOf(
            LoginDataModel { loginState ->
                currentLoginState = loginState
                when (loginState) {
                    is SuccessState<*> -> {
                        userDataModel.activate()
                    }
                    else -> Unit
                }
            }.apply {
                praxisCommand = { newCommand ->
                    currentNavigationCommand = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            if (newCommand.screen == HarvestRoutes.Screen.ORG_USER_DASHBOARD) {
                                navController clearBackStackAndNavigateTo newCommand.screen.withOrgId(
                                    identifier = orgIdentifier,
                                    id = null
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    var currentErrorMsg: String? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = currentLoginState, key2 = userState) {
        currentErrorMsg = when {
            currentLoginState is ErrorState -> (currentLoginState as ErrorState).throwable.message
            userState is ErrorState -> (userState as ErrorState).throwable.message
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

