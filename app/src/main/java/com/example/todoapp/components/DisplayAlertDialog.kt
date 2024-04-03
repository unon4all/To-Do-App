package com.example.todoapp.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.todoapp.R

/**
 * Composable function to display an AlertDialog with custom title, message, and buttons.
 *
 * @param title Title of the AlertDialog.
 * @param message Message to be displayed in the AlertDialog.
 * @param openDialog Boolean to indicate whether the dialog should be open or not.
 * @param closeDialog Callback function to close the AlertDialog.
 * @param onYesClicked Callback function to handle action when 'Yes' button is clicked.
 */
@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            // Dismiss the dialog when clicking outside or pressing back
            onDismissRequest = { closeDialog() },
            // Button for confirming the action
            confirmButton = {
                Button(
                    onClick = {
                        // Execute 'Yes' action
                        onYesClicked()
                        // Close the dialog
                        closeDialog()
                    }
                ) {
                    // Text for the 'Yes' button
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            // Title of the dialog
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            // Message body of the dialog
            text = {
                Text(
                    text = message,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            },
            // Button for dismissing the dialog without action
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        // Close the dialog without action
                        closeDialog()
                    }
                ) {
                    // Text for the 'No' button
                    Text(text = stringResource(id = R.string.no))
                }
            }
        )
    }
}
