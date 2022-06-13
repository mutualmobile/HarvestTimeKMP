package com.mutualmobile.harvestKmp.android.ui.screens.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme
import com.mutualmobile.harvestKmp.android.ui.utils.get

@Composable
fun CommonAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    titleProvider: @Composable () -> String,
    bodyTextProvider: @Composable () -> String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        text = {
            Text(text = bodyTextProvider(), style = MaterialTheme.typography.body1)
        },
        title = {
            Text(text = titleProvider(), style = MaterialTheme.typography.h6)
        }
    )
}

@Preview
@Composable
fun CommonAlertDialogPreview() {
    HarvestKmpTheme {
        CommonAlertDialog(
            onDismiss = {},
            onConfirm = {},
            titleProvider = { MR.strings.delete_work_dialog_title.get() },
            bodyTextProvider = { MR.strings.delete_work_dialog_bodyText.get() }
        )
    }
}