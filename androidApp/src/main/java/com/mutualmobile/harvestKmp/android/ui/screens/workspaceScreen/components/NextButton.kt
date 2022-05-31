package com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator

@Composable
fun NextButton(
  modifier: Modifier = Modifier,
  composeNavigator: ComposeNavigator
) {
  Button(
    onClick = {
    },
    modifier
      .fillMaxWidth()
      .height(50.dp)
      .padding(top = 8.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = MaterialTheme.colors.surface
    )
  ) {
    Text(
      text = "Next"
    )
  }
}