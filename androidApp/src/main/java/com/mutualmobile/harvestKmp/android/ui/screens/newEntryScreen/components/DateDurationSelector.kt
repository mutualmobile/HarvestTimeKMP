package com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.MR
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DateDurationSelector(onDurationChange: (String) -> Unit) {
    val formatter = SimpleDateFormat("EEEE, d MMMM ", Locale.getDefault())

    var isDatePickerVisible by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("Wednesday, 11 May") }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DateDurationSelectorItem(
                title = stringResource(MR.strings.dds_screen_date_title.resourceId)
            ) {
                var isDateIconPressed by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .combinedClickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {},
                        )
                        .pointerInput(Unit) {
                            this.detectTapGestures(
                                onPress = {
                                    isDateIconPressed = true
                                    isDateIconPressed = try {
                                        awaitRelease()
                                        isDatePickerVisible = true
                                        false
                                    } catch (e: GestureCancellationException) {
                                        false
                                    }
                                }
                            )
                        }
                ) {
                    Text(
                        text = selectedDate,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Normal
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.alpha(0.5f),
                        tint = if (isDateIconPressed) MaterialTheme.colors.primary
                        else MaterialTheme.colors.onSurface
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .alpha(0.5f)
            )
            DateDurationSelectorItem(
                title = stringResource(MR.strings.dds_screen_duration_title.resourceId)
            ) {
                var durationEtText by remember { mutableStateOf("") }
                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current
                TextField(
                    value = durationEtText,
                    onValueChange = { updatedDuration ->
                        durationEtText = updatedDuration
                        onDurationChange(durationEtText)
                    },
                    modifier = Modifier.fillMaxWidth(0.25f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.body1.copy(textAlign = TextAlign.End),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                )
            }
        }
    }
    if (isDatePickerVisible) {
        DatePicker(onDateSelected = {
            selectedDate = formatter.format(it)
        }, onDismissRequest = { isDatePickerVisible = false })
    }
}

@Composable
private fun DateDurationSelectorItem(
    title: String,
    endContent: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.alpha(0.5f),
            fontWeight = FontWeight.Normal
        )
        endContent()
    }
}