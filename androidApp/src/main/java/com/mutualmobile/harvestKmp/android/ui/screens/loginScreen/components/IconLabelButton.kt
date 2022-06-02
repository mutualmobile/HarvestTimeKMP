package com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val CardShape = RoundedCornerShape(8.dp)

@Composable
fun IconLabelButton(
    modifier: Modifier=Modifier,
    @DrawableRes icon: Int? = null,
    label: String,
    isLoading: Boolean = false,
    errorMsg: String? = null,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.animateContentSize()
    ) {
        Button(
            onClick = onClick, colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            shape = CardShape
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    icon?.let { nnIcon ->
                        Image(
                            painter = painterResource(id = nnIcon),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                AnimatedVisibility(visible = isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
        errorMsg?.let { nnErrorMsg ->
            Text(
                text = nnErrorMsg,
                style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.error),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}