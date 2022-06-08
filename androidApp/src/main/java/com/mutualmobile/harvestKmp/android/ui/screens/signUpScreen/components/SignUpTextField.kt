package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.CardShape


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    isPasswordTextField: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = CardShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.25f),
            cursorColor = MaterialTheme.colors.surface,
            placeholderColor = MaterialTheme.colors.secondary,
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.50f)

        ),
        visualTransformation = if (isPasswordTextField) PasswordVisualTransformation()
        else VisualTransformation.None,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = if (isPasswordTextField) KeyboardType.Password else KeyboardType.Email,
            imeAction = if (isPasswordTextField) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardManager?.hide()
            },
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        label = {
            Text(
                placeholderText,
                color = Color.Gray.copy(alpha = 0.50f),
            )
        }
    )
}

