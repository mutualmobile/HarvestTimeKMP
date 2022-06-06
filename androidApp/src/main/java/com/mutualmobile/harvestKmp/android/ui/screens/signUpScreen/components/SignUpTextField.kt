package com.mutualmobile.harvestKmp.android.ui.screens.signUpScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.screens.loginScreen.components.CardShape


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String="",
    isPasswordTextField: Boolean = false,
    hintText:String=""
) {
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholderText) },
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
                hintText,
                color = Color.Gray.copy(alpha = 0.50f),
            )
        }
    )
}

