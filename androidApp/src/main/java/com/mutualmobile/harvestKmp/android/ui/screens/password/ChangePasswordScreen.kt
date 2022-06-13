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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components.SignUpTextField
import com.mutualmobile.harvestKmp.android.ui.utils.clearBackStackAndNavigateTo
import com.mutualmobile.harvestKmp.android.ui.utils.showToast
import com.mutualmobile.harvestKmp.android.viewmodels.ChangePasswordViewModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.get

@Composable
fun ChangePasswordScreen(
    navController: NavHostController,
    cpVm: ChangePasswordViewModel = get()
) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    
    LaunchedEffect(cpVm.changePasswordPraxisCommand) {
        when (cpVm.changePasswordPraxisCommand) {
            is NavigationPraxisCommand -> {
                if ((cpVm.changePasswordPraxisCommand as NavigationPraxisCommand).screen.isBlank()) {
                    navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(MR.strings.change_password.resourceId),
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
                .padding(bodyPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SignUpTextField(
                    value = cpVm.oldPassword,
                    onValueChange = { updatedString -> cpVm.oldPassword = updatedString },
                    placeholderText = stringResource(MR.strings.old_password_et_title.resourceId),
                    isPasswordTextField = true
                )
                SignUpTextField(
                    value = cpVm.newPassword,
                    onValueChange = { updatedString -> cpVm.newPassword = updatedString },
                    placeholderText = stringResource(MR.strings.new_password_et_title.resourceId),
                    isPasswordTextField = true
                )

                IconLabelButton(
                    label = stringResource(MR.strings.request_reset_password.resourceId),
                    errorMsg = (cpVm.changePasswordState as? ErrorState)?.throwable?.message,
                    isLoading = cpVm.changePasswordState is LoadingState,
                    onClick =
                    {
                        cpVm.changePasswordDataModel.changePassWord(
                            cpVm.newPassword.trim(),
                            cpVm.oldPassword.trim(),
                        ).onEach { passwordState ->
                            cpVm.changePasswordState = passwordState
                            when (passwordState) {
                                is SuccessState<*> -> {
                                    ctx.showToast("Change password successful!")
                                    cpVm.resetAll {
                                        navController.navigateUp()
                                    }
                                }
                                else -> Unit
                            }
                        }.launchIn(coroutineScope)
                    })
            }
            HarvestDialog(
                praxisCommand = cpVm.changePasswordPraxisCommand,
                onConfirm = {
                    cpVm.changePasswordPraxisCommand = null
                },
            )
        }
    }
}