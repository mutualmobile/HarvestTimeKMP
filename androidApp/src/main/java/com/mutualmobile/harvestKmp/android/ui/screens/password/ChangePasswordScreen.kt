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
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.ChangePasswordDataModel
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(navController: NavHostController) {
    val ctx = LocalContext.current
    var changePasswordState: DataState by remember { mutableStateOf(EmptyState) }
    val scope = rememberCoroutineScope()

    var changePasswordPraxisCommand: PraxisCommand? by remember { mutableStateOf(null) }
    val changePasswordDataModel by remember {
        mutableStateOf(
            ChangePasswordDataModel {}.apply {
                praxisCommand = { newCommand ->
                    changePasswordPraxisCommand = newCommand
                    when (newCommand) {
                        is NavigationPraxisCommand -> {
                            if (newCommand.screen.isBlank()) {
                                navController clearBackStackAndNavigateTo HarvestRoutes.Screen.FIND_WORKSPACE
                            }
                        }
                    }
                }
            }
        )
    }
    var currentPassword by remember { mutableStateOf("") }
    var currentConfirmPassword by remember { mutableStateOf("") }

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
                    value = currentPassword,
                    onValueChange = { updatedString -> currentPassword = updatedString },
                    placeholderText = stringResource(MR.strings.password_et_placeholder.resourceId)
                )
                SignUpTextField(
                    value = currentConfirmPassword,
                    onValueChange = { updatedString -> currentConfirmPassword = updatedString },
                    placeholderText = stringResource(MR.strings.signup_screen_confirm_password_et_placeholder.resourceId)
                )

                IconLabelButton(
                    label = stringResource(MR.strings.request_reset_password.resourceId),
                    errorMsg = (changePasswordState as? ErrorState)?.throwable?.message,
                    isLoading = changePasswordState is LoadingState,
                    onClick =
                    {
                        scope.launch {
                            changePasswordDataModel.changePassWord(
                                currentPassword.trim(),
                                currentConfirmPassword.trim()
                            ).collect { passwordState ->
                                changePasswordState = passwordState
                                when (passwordState) {
                                    is SuccessState<*> -> {
                                        ctx.showToast("Change password successful!")
                                        navController.navigateUp()
                                    }
                                    else -> Unit
                                }
                            }

                        }

                    })
            }
            HarvestDialog(
                praxisCommand = changePasswordPraxisCommand,
                onConfirm = {
                    changePasswordPraxisCommand = null
                },
            )
        }
    }
}