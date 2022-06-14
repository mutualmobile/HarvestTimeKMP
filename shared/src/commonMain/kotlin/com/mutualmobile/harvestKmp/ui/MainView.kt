package com.mutualmobile.harvestKmp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.getPlatformName

@Composable
fun MainView() {
  var text by remember { mutableStateOf("Hello, World!") }
  val platformName = getPlatformName()

  Button(
    modifier = Modifier.padding(top = 100.dp),
    onClick = {
      text = "Hello, $platformName"
    }) {
    Text(text)
  }
}