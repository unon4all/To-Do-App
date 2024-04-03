package com.example.todoapp.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.components.DisplayAlertDialog
import com.example.todoapp.components.PriorityItem
import com.example.todoapp.data.models.Priority
import com.example.todoapp.ui.theme.LARGE_PADDING
import com.example.todoapp.ui.viewModels.SharedViewModels
import com.example.todoapp.utils.Action
import com.example.todoapp.utils.SearchAppBarState
import com.example.todoapp.utils.SearchAppBarState.CLOSED
import com.example.todoapp.utils.SearchAppBarState.OPENED

@Composable
fun ListAppBar(
    sharedViewModels: SharedViewModels,
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
) {

    when (searchAppBarState) {
        CLOSED -> {
            DefaultListAppBar(onSearchClicked = {
                sharedViewModels.updateSearchAppBarState(newState = OPENED)
            },
                onSortClick = { sharedViewModels.persistSortState(it) },
                onDeleteAllConfirmed = { sharedViewModels.updateAction(newAction = Action.DELETE_ALL) })
        }

        else -> {
            SearchAppBar(text = searchTextState,
                onTextChange = { newText -> sharedViewModels.updateSearchTextState(newText = newText) },
                onSearchClicked = { searchQuery ->
                    sharedViewModels.searchDatabase(searchQuery = searchQuery)
                },
                onCloseClicked = {
                    sharedViewModels.updateSearchAppBarState(newState = CLOSED)
                    sharedViewModels.updateSearchTextState(newText = "")
                })
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit = {},
    onSortClick: (Priority) -> Unit = {},
    onDeleteAllConfirmed: () -> Unit = {}
) {

    TopAppBar(title = {
        Text(
            text = stringResource(id = R.string.task_title),
            color = MaterialTheme.colorScheme.onPrimary
        )
    },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClick = onSortClick,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )

}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit, onSortClick: (Priority) -> Unit, onDeleteAllConfirmed: () -> Unit
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(title = stringResource(id = R.string.delete_all_tasks),
        message = stringResource(id = R.string.delete_all_tasks_confirmation),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {
            onDeleteAllConfirmed()
        })

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClick = onSortClick)
    DeleteAllActions(onDeleteAllConfirmed = { openDialog = true })
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_action),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun SortAction(onSortClick: (Priority) -> Unit) {

    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_filter_list),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.entries.toTypedArray().slice(setOf(0, 2, 3)).forEach { priority ->
                DropdownMenuItem(text = { PriorityItem(priority = priority) }, onClick = {
                    expanded = false
                    onSortClick(priority)
                })
            }
        }
    }
}

@Composable
fun DeleteAllActions(onDeleteAllConfirmed: () -> Unit) {

    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expanded = true }) {

        Icon(
            painter = painterResource(id = R.drawable.baseline_vertical_menu),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

            DropdownMenuItem(text = {
                Text(
                    text = stringResource(id = R.string.delete_tasks),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = LARGE_PADDING)
                )
            }, onClick = {
                expanded = false
                onDeleteAllConfirmed()
            })

        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {

    TextField(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp),
        value = text,
        onValueChange = {
            onTextChange(it)
            onSearchClicked(it)
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_placeholder),
                color = Color.White,
                modifier = Modifier.alpha(0.5f)
            )
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        ),
        singleLine = true,
        leadingIcon = {
            IconButton(modifier = Modifier.alpha(0.38f), onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search_button),
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    onTextChange("")
                } else {
                    onCloseClicked()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.close_search_field),
                )
            }
        })
}