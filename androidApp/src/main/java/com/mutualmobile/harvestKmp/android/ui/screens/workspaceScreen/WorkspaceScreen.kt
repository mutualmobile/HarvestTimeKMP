package com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.ComposeNavigator
import com.mutualmobile.harvestKmp.MR.strings.subtitle_this_address_harvest
import com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components.WorkspaceInputView

@Composable
fun WorkspaceInputUI(composeNavigator: ComposeNavigator) {
  CommonInputUI(
    composeNavigator,
    {
      WorkspaceInputView(it)
    },
    stringResource(subtitle_this_address_harvest.resourceId)
  )
}

@Composable
fun TextHttps() {
  Text(
    text = "https://"
  )
}

@Composable
fun TextHarvestCom() {
  Text(
    ".harvest.com",
    overflow = TextOverflow.Clip,
    maxLines = 1
  )
}
