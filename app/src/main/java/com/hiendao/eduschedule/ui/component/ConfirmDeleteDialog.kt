package com.hiendao.eduschedule.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ConfirmDeleteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    descriptionText: String = "",
    confirmButton: String = "",
    dismissButton: String = "",
    title: String = ""
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = descriptionText)
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = confirmButton)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = dismissButton)
            }
        },
        modifier = modifier
    )

}