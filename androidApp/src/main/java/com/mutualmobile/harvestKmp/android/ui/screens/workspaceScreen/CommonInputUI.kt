package com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.ComposeNavigator
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components.NextButton
import com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components.SubTitleText
import com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components.WorkspaceInputView

@Composable
fun CommonInputUI(
  composeNavigator: ComposeNavigator,
  TopView: @Composable (modifier: Modifier) -> Unit,
  subtitleText: String
) {
  val scaffoldState = rememberScaffoldState()

  Scaffold(
    backgroundColor = MaterialTheme.colors.background,
    contentColor = MaterialTheme.colors.onBackground,
    modifier = Modifier
      .statusBarsPadding()
      .navigationBarsPadding(),
    scaffoldState = scaffoldState,
    snackbarHost = {
      scaffoldState.snackbarHostState
    }
  ) { innerPadding ->
    Box(modifier = Modifier.padding(innerPadding)) {
      ConstraintLayout(
        modifier = Modifier
          .padding(12.dp)
          .navigationBarsWithImePadding()
          .fillMaxHeight()
          .fillMaxWidth()
      ) {
        // Create references for the composables to constrain
        val (inputView, subtitle, button) = createRefs()

        TopView(modifier = Modifier.constrainAs(inputView) {
          top.linkTo(parent.top)
          bottom.linkTo(button.top)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        })
        SubTitleText(modifier = Modifier.constrainAs(subtitle) {
          top.linkTo(inputView.bottom)
        }, subtitleText)
        NextButton(modifier = Modifier.constrainAs(button) {
          bottom.linkTo(parent.bottom)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }, composeNavigator)
      }
    }

  }
}


