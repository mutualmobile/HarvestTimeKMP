package com.mutualmobile.harvestKmp.android.ui.screens.workspaceScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.mutualmobile.harvestKmp.android.ui.theme.Typography

@Composable
fun SubTitleText(
  modifier: Modifier = Modifier,
  subtitleText: String
) {
  Text(
    subtitleText,
    modifier = modifier
      .fillMaxWidth()
      .wrapContentWidth(align = Alignment.Start),
    style = Typography.subtitle2.copy(
      color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
      fontWeight = FontWeight.Normal,
      textAlign = TextAlign.Start
    )
  )
}