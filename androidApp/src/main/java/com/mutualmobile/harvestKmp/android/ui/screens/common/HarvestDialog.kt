package com.mutualmobile.harvestKmp.android.ui.screens.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.theme.HarvestKmpTheme
import com.mutualmobile.harvestKmp.android.ui.utils.get
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand

@Composable
fun HarvestDialog(
    praxisCommand: PraxisCommand?,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    val command = praxisCommand as? ModalPraxisCommand
    val title by remember(command) { mutableStateOf(command?.title) }
    val text by remember(command) { mutableStateOf(command?.message.orEmpty()) }

    if (command != null) {
        AlertDialog(
            onDismissRequest = { onDismiss?.invoke() },
            title = {
                Text(
                    text = title ?: MR.strings.harvest_dialog_title.get(),
                    style = MaterialTheme.typography.h6
                )
            },
            text = { Text(text = text, style = MaterialTheme.typography.body1) },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text(text = MR.strings.harvest_dialog_confirm_btn_txt.get())
                }
            },
            dismissButton = {
                onDismiss?.let { nnOnDismiss ->
                    TextButton(onClick = { nnOnDismiss() }) {
                        Text(text = MR.strings.harvest_dialog_dismiss_btn_txt.get())
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun HarvestDialogPreview() {
    HarvestKmpTheme {
        HarvestDialog(
            praxisCommand = ModalPraxisCommand(
                title = "Error",
                message = "An account with these credentials already exists"
            ),
            onConfirm = {},
        )
    }
}