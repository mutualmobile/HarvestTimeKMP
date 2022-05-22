package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.android.ui.theme.WeekSwitchButtonBgColor

enum class WeekSwitcherType {
    Week, Semimonth, Month, Quarter, Year
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeekSwitcher(onWeekIncreased: () -> Unit, onWeekDecreased: () -> Unit) {
    val focusManager = LocalFocusManager.current

    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    var currentDropDownMenuItem by remember { mutableStateOf(WeekSwitcherType.Week) }

    LaunchedEffect(isDropDownMenuExpanded) {
        if (!isDropDownMenuExpanded) {
            focusManager.clearFocus()
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        WeekSwitchButton(isBackButton = true, onClick = onWeekDecreased)
        WeekSwitchButton(onClick = onWeekIncreased)

        ExposedDropdownMenuBox(
            expanded = isDropDownMenuExpanded,
            onExpandedChange = { isExpanded -> isDropDownMenuExpanded = isExpanded },
        ) {
            TextField(
                value = currentDropDownMenuItem.name,
                onValueChange = {},
                readOnly = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                trailingIcon = {
                    val trailingIconRotation by animateFloatAsState(
                        targetValue = if (isDropDownMenuExpanded) 180f else 0f
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(trailingIconRotation)
                    )
                }
            )

            ExposedDropdownMenu(expanded = isDropDownMenuExpanded, onDismissRequest = {
                isDropDownMenuExpanded = false
            }) {
                WeekSwitcherType.values().forEach { weekSwitcherType ->
                    DropdownMenuItem(onClick = {
                        currentDropDownMenuItem = weekSwitcherType
                        isDropDownMenuExpanded = false
                    }) {
                        Text(text = weekSwitcherType.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekSwitchButton(
    isBackButton: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        backgroundColor = WeekSwitchButtonBgColor,
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .alpha(0.65f)
                .rotate(if (isBackButton) 180f else 0f),
        )
    }
}