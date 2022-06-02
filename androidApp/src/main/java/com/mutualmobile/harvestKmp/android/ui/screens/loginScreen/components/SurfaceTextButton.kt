package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SurfaceTextButton(
    text: String,
    verticalPadding: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(8.dp),
    style: TextStyle = MaterialTheme.typography.body2,
    fontWeight: FontWeight? = style.fontWeight,
    onClick: () -> Unit = {},
) {
    Text(
        text = text,
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(vertical = verticalPadding, horizontal = 8.dp),
        color = MaterialTheme.colors.surface,
        style = style,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SurfaceTextButton(
    text: AnnotatedString,
    verticalPadding: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(8.dp),
    style: TextStyle = MaterialTheme.typography.body2,
    fontWeight: FontWeight? = style.fontWeight,
    onClick: () -> Unit = {},
) {
    Text(
        text = text,
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(vertical = verticalPadding, horizontal = 8.dp),
        color = MaterialTheme.colors.surface,
        style = style,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center
    )
}