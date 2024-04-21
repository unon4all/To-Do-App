package com.example.todoapp.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.todoapp.R
import com.example.todoapp.viewModels.SharedViewModels
import com.example.todoapp.utils.Action
import com.example.todoapp.utils.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreens(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModels: SharedViewModels,
) {

    val allTask by sharedViewModels.allTask.collectAsState()
    val sortState by sharedViewModels.sortState.collectAsState()

    val lowPriorityTasks by sharedViewModels.lowPriorityTask.collectAsState()
    val highPriorityTasks by sharedViewModels.highPriorityTask.collectAsState()

    val searchAppBarState: SearchAppBarState = sharedViewModels.searchAppBarState
    val searchTextState: String = sharedViewModels.searchTextState

    val searchedTask by sharedViewModels.searchedTask.collectAsState()


    val snackBarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(snackBarHostState = snackBarHostState,
        onComplete = { completeAction -> sharedViewModels.updateAction(newAction = completeAction) },
        onUndoClicked = { undoAction -> sharedViewModels.updateAction(newAction = undoAction) },
        taskTitle = sharedViewModels.title,
        action = action,
        handleDatabaseActions = { sharedViewModels.handleDatabaseActions(action) })

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            ListAppBar(
                sharedViewModels = sharedViewModels,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        floatingActionButton = {
            ListFAB(onFabClicked = navigateToTaskScreen)
        },
        content = {
            ListContent(modifier = Modifier.padding(it),
                allTasks = allTask,
                searchedTasks = searchedTask,
                searchAppBarState = searchAppBarState,
                navigateTaskScreen = navigateToTaskScreen,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                onSwipeToDelete = { action, toDoTask ->
                    sharedViewModels.updateAction(newAction = action)
                    sharedViewModels.updateTaskField(selectedTask = toDoTask)
                    snackBarHostState.currentSnackbarData?.dismiss()
                })
        },
    )

}


@Composable
fun ListFAB(onFabClicked: (taskId: Int) -> Unit) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
        )
    }
}


@Composable
fun DisplaySnackBar(
    snackBarHostState: SnackbarHostState,
    handleDatabaseActions: () -> Unit,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action,
) {

    handleDatabaseActions()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = setMessage(action, taskTitle), actionLabel = setActionLabel(action)
                )

                if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
                    onUndoClicked(Action.UNDO)
                } else if (snackBarResult == SnackbarResult.Dismissed || action != Action.DELETE) {
                    onComplete(Action.NO_ACTION)
                }
            }
        }
    }
}

private fun setMessage(action: Action, taskTitle: String): String {
    return when (action) {
        Action.DELETE_ALL -> "All Task Removed"
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}



