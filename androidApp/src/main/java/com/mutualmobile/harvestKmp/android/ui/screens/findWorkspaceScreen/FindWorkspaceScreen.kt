package com.mutualmobile.harvestKmp.android.ui.screens.findWorkspaceScreen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.common.HarvestDialog
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.IconLabelButton
import com.mutualmobile.harvestKmp.android.ui.theme.DrawerBgColor
import com.mutualmobile.harvestKmp.android.ui.theme.FindWorkspaceScreenTypography
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme
import com.mutualmobile.harvestKmp.android.ui.utils.get
import com.mutualmobile.harvestKmp.android.viewmodels.FindWorkspaceViewModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import org.koin.androidx.compose.get

@Composable
fun FindWorkspaceScreen(
    navController: NavHostController,
    fwVm: FindWorkspaceViewModel = get()
) {
    LaunchedEffect(fwVm.currentFindOrgNavigationCommand) {
        when (fwVm.currentFindOrgNavigationCommand) {
            is NavigationPraxisCommand -> {
                val destination = (fwVm.currentFindOrgNavigationCommand as NavigationPraxisCommand).screen
                fwVm.resetAll { navController.navigate(destination) }
            }
        }
    }

    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DrawerBgColor)
                .padding(16.dp)
                .navigationBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = MR.strings.find_workspace_screen_title.get(),
                    style = FindWorkspaceScreenTypography.caption
                )
                CompositionLocalProvider(LocalTextStyle provides FindWorkspaceScreenTypography.h6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(text = "https://")
                        WorkspaceTextField(
                            value = fwVm.tfValue,
                            onValueChanged = { updatedString ->
                                fwVm.tfValue = updatedString
                            }
                        )
                        Text(text = ".harvestclone.com")
                    }
                }
                Text(
                    text = MR.strings.find_workspace_screen_subtitle.get(),
                    style = FindWorkspaceScreenTypography.subtitle1
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                IconLabelButton(
                    label = MR.strings.find_workspace_screen_btn_txt.get(),
                    modifier = Modifier
                        .fillMaxWidth(if (fwVm.findOrgState is ErrorState) 0.45f else 1f)
                        .padding(vertical = 4.dp),
                    isLoading = fwVm.findOrgState is LoadingState,
                    errorMsg = when (fwVm.findOrgState) {
                        is ErrorState -> (fwVm.findOrgState as ErrorState).throwable.message
                            ?: MR.strings.generic_error_msg.get()
                        else -> null
                    },
                    onClick = {
                        fwVm.findOrgByIdentifierDataModel.findOrgByIdentifier(identifier = fwVm.tfValue)
                    },
                )
                if (fwVm.findOrgState is ErrorState) {
                    IconLabelButton(
                        label = MR.strings.find_workspace_screen_signup_btn_txt.get(),
                        modifier = Modifier
                            .fillMaxWidth(if (fwVm.findOrgState is ErrorState) 0.9f else 0f)
                            .padding(vertical = 4.dp),
                        onClick = {
                            navController.navigate(HarvestRoutes.Screen.NEW_ORG_SIGNUP)
                        },
                    )
                }
            }
            HarvestDialog(
                praxisCommand = fwVm.currentFindOrgNavigationCommand,
                onConfirm = {
                    fwVm.currentFindOrgNavigationCommand = null
                },
            )
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun WorkspaceTextField(
    value: String,
    onValueChanged: (String) -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = FindWorkspaceScreenTypography.h6.copy(color = Color.White),
        decorationBox = { nonEmptyTextComposable ->
            AnimatedContent(
                targetState = value.isEmpty(),
                transitionSpec = { fadeIn() with fadeOut() }
            ) { tfValueIsBlank ->
                if (tfValueIsBlank) {
                    Text(
                        text = MR.strings.find_workspace_screen_placeholder_txt.get(),
                        color = Color.White
                    )
                } else {
                    nonEmptyTextComposable()
                }
            }
        },
        cursorBrush = SolidColor(LocalContentColor.current),
        modifier = Modifier.width(IntrinsicSize.Max)
    )
}

@Preview(
    device = "id:pixel_4", showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun FindWorkspaceScreenPreview() {
    HarvestKmpTheme {
        FindWorkspaceScreen(navController = rememberNavController())
    }
}
