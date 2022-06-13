package com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mutualmobile.harvestKmp.MR

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BucketSelector(
    currentProject: String,
    onWorkClick: () -> Unit,
    onDepartmentClick: () -> Unit = {},
    note: String,
    onNoteChanged: (String) -> Unit
) {
    val screenConfig = LocalConfiguration.current
    var isStarClicked by remember { mutableStateOf(false) }
    val starButtonTint by animateColorAsState(
        targetValue = if (isStarClicked) MaterialTheme.colors.primary.copy(alpha = 0.75f)
        else MaterialTheme.colors.onSurface.copy(0.3f)
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "MUTUAL MOBILE",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Divider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                thickness = 2.dp
            )
            Box(contentAlignment = Alignment.CenterStart) {
                Column {
                    BucketItem(title = currentProject,
                        onClick = { onDepartmentClick() })
                    Divider(
                        modifier = Modifier.alpha(0.5f),
                        startIndent = screenConfig.screenWidthDp.times(0.1f).dp,
                    )
                    BucketItem(title = "Work")
                    Divider(
                        modifier = Modifier.alpha(0.5f),
                        startIndent = 16.dp,
                    )
                }
                IconButton(onClick = { isStarClicked = !isStarClicked }) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = starButtonTint,
                    )
                }
            }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            TextField(
                value = note,
                onValueChange = onNoteChanged,
                placeholder = {
                    Text(
                        text = stringResource(MR.strings.new_entry_screen_notes_et_hint.resourceId),
                        modifier = Modifier.alpha(0.5f)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp, start = 8.dp),
                singleLine = true,
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

@Composable
private fun BucketItem(
    title: String,
    endIcon: ImageVector = Icons.Default.KeyboardArrowRight,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.onSurface
            ),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(16.dp),
            maxLines = 1
        )
        Icon(
            imageVector = endIcon,
            contentDescription = null,
            modifier = Modifier.padding(end = 24.dp),
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        )
    }
}