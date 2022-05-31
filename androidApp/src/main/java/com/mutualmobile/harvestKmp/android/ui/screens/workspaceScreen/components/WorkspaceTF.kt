package com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize.Max
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion
import androidx.compose.ui.unit.dp

@Composable
fun WorkspaceTF() {
  var workspace by remember { mutableStateOf("") }

  BasicTextField(
    value = workspace,
    onValueChange = { newEmail -> workspace = newEmail },
    singleLine = true,
    modifier = Modifier
      .width(Min)
      .padding(top = 12.dp, bottom = 12.dp),
    maxLines = 1,
    cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
    decorationBox = { inputTf ->
      Box {
        if (workspace.isEmpty()) {
          Text(
            text = "your-workspace",
            textAlign = TextAlign.Start,
            modifier = Modifier.width(Max),
          )
        } else {
          inputTf()
        }
      }
    }
  )
}