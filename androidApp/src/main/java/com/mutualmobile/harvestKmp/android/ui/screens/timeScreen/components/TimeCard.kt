package com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.ui.theme.TimeScreenTypography
import com.mutualmobile.harvestKmp.android.ui.utils.toDecimalString

@Composable
fun TimeCard(
    organisationName: String,
    bucketName: String,
    time: Float,
    taskType: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4.dp,
        shape = RoundedCornerShape(0)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = organisationName, style = TimeScreenTypography.subtitle1)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(LocalTextStyle provides TimeScreenTypography.h2) {
                    Text(text = bucketName)
                    Text(text = time.toDecimalString())
                }
            }
            Text(text = taskType, style = TimeScreenTypography.body1)
        }
    }
}