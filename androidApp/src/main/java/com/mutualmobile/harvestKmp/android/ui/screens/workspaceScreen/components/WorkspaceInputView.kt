package com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.TextHttps
import com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.TextHarvestCom

@Composable
fun WorkspaceInputView(modifier: Modifier) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentWidth()
  ) {
    Text(
      text = "Workspace URL", style = MaterialTheme.typography.body1.copy(
      color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
      fontWeight = FontWeight.Normal,
      textAlign = TextAlign.Start
    ), modifier = Modifier.padding(bottom = 4.dp)
    )
    Row(
      modifier = modifier
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start
    ) {
      TextHttps()
      WorkspaceTF()
      TextHarvestCom()
    }
  }
}