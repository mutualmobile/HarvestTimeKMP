package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.mutualmobile.harvestKmp.android.viewmodels.LoginViewModel
import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import org.koin.androidx.compose.get

@Composable
fun LoginScreen(
    navController: NavHostController,
    userState: DataState,
    lVm: LoginViewModel = get(),
    onLoginSuccess: () -> Unit
) {
    val ctx = LocalContext.current
    
    LaunchedEffect(lVm.currentNavigationCommand) {
        when (lVm.currentNavigationCommand) {
            is NavigationPraxisCommand -> {
                onLoginSuccess()
            }
        }
    }

    LaunchedEffect(userState) {
        if (userState is PraxisDataModel.SuccessState<*>) {
            if ((userState.data as? GetUserResponse) != null) {
                if ((userState.data as? GetUserResponse)?.role != UserRole.ORG_USER.role) {
                    lVm.logout()
                } else {
                    if (lVm.currentNavigationCommand is NavigationPraxisCommand) {
                        val destination = (lVm.currentNavigationCommand as NavigationPraxisCommand).screen
                        lVm.resetAll {
                            navController clearBackStackAndNavigateTo destination
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(lVm.currentLoginState) {
        lVm.currentErrorMsg = when (lVm.currentLoginState) {
            is ErrorState -> (lVm.currentLoginState as ErrorState).throwable.message
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
                value = lVm.currentWorkEmail,
                onValueChange = { updatedString -> lVm.currentWorkEmail = updatedString },
                placeholderText = stringResource(MR.strings.login_screen_email_et_placeholder.resourceId)
            )
            SignInTextField(
                value = lVm.currentPassword,
                onValueChange = { updatedString -> lVm.currentPassword = updatedString },
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
                onClick = { lVm.login() },
                isLoading = lVm.currentLoginState is LoadingState || userState is LoadingState,
                errorMsg = lVm.currentErrorMsg,
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
            praxisCommand = lVm.currentNavigationCommand,
            onConfirm = {
                if (lVm.currentNavigationCommand is ModalPraxisCommand) {
                    if ((lVm.currentNavigationCommand as ModalPraxisCommand).title == "Work in Progress") {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://harvestkmp.web.app/")
                        }
                        ctx.startActivity(intent)
                    }
                }
                lVm.currentNavigationCommand = null
            },
            onDismiss = {
                lVm.currentNavigationCommand = null
            }
        )
    }
}