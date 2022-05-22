package com.mutualmobile.harvestKmp.android.ui.screens.settingsScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.android.ui.theme.PrimaryLightColor
import com.mutualmobile.harvestKmp.android.ui.theme.SwitchCheckedTrackColor
import com.mutualmobile.harvestKmp.android.ui.theme.SwitchUncheckedThumbColor
import com.mutualmobile.harvestKmp.android.ui.theme.SwitchUncheckedTrackColor

@Composable
fun SettingsListItem(
    title: String,
    subtitle: String = "",
    isCheckable: Boolean = false,
    showTopDivider: Boolean = false,
    showBottomDivider: Boolean = false,
    onCheckChanged: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    var isEnabled by remember { mutableStateOf(false) }
    Column {
        if (showTopDivider) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isEnabled = !isEnabled
                    onClick()
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.body1)
                if (subtitle.isNotBlank()) {
                    Text(text = subtitle, style = MaterialTheme.typography.body2)
                }
            }
            if (isCheckable) {
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { isChecked ->
                        isEnabled = isChecked
                        onCheckChanged(isEnabled)
                        onClick()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = PrimaryLightColor,
                        checkedTrackColor = SwitchCheckedTrackColor,
                        uncheckedTrackColor = SwitchUncheckedTrackColor,
                        uncheckedThumbColor = SwitchUncheckedThumbColor,
                    ),
                    enabled = true
                )
            }
        }
        if (showBottomDivider) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
            )
        }
    }
}