package com.mutualmobile.harvestKmp.android.ui.screens.projectScreen.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.utils.get

@Composable
fun SearchView(state: TextFieldValue, onStateChanged: (TextFieldValue) -> Unit) {
    var stateSearch by remember { mutableStateOf(false) }

    TextField(
        value = state,
        onValueChange = onStateChanged,
        readOnly = stateSearch.not(),
        modifier = Modifier
            .then(
                if (stateSearch) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.width(100.dp)
                }
            )
            .defaultMinSize(
                minWidth = 100.dp,
            ),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        trailingIcon = {
            if (state != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        stateSearch = false
                        onStateChanged(TextFieldValue("")) // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            } else {

                IconButton(
                    onClick = {
                        stateSearch = true
                        onStateChanged(TextFieldValue("")) // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }

            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.colorPrimary),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        placeholder = {
            if (stateSearch) {
                Text(
                    text = MR.strings.project_screen_placeholder_txt.get(),
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchViewPreview() {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    SearchView(textState) {
        textState = it
    }
}