package com.example.todoapp.ui.screens.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.todoapp.R
import com.example.todoapp.components.DisplayAlertDialog
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.ui.theme.LARGEST_PADDING
import com.example.todoapp.ui.theme.LARGE_PADDING
import com.example.todoapp.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.todoapp.ui.theme.TASK_ITEM_ELEVATION
import com.example.todoapp.utils.Action
import com.example.todoapp.utils.RequestState
import com.example.todoapp.utils.SearchAppBarState

@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    allTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortState: RequestState<Priority>,
    searchedTasks: RequestState<List<ToDoTask>>,
    searchAppBarState: SearchAppBarState,
    navigateTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, ToDoTask) -> Unit
) {

    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateTaskScreen = navigateTaskScreen,
                        modifier = modifier,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }

            sortState.data == Priority.NONE -> {

                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = allTasks.data,
                        navigateTaskScreen = navigateTaskScreen,
                        modifier = modifier,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }

            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriorityTasks,
                    navigateTaskScreen = navigateTaskScreen,
                    modifier = modifier,
                    onSwipeToDelete = onSwipeToDelete
                )
            }


            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriorityTasks,
                    navigateTaskScreen = navigateTaskScreen,
                    modifier = modifier,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
        }
    }

}


@Composable
fun HandleListContent(
    modifier: Modifier = Modifier,
    tasks: List<ToDoTask>,
    navigateTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, ToDoTask) -> Unit
) {

    if (tasks.isEmpty()) EmptyContent() else DisplayTask(
        modifier = modifier,
        toDoTask = tasks,
        navigateTaskScreen = navigateTaskScreen,
        onSwipeToDelete = onSwipeToDelete
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTask(
    modifier: Modifier = Modifier,
    toDoTask: List<ToDoTask>,
    navigateTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, ToDoTask) -> Unit
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

    LazyColumn(modifier = modifier) {
        items(items = toDoTask, key = { task -> task.id }) { selectedTask ->

            val swipedTaskState = remember { mutableStateOf<ToDoTask?>(null) }

            val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { dismissValue ->
                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                    swipedTaskState.value = selectedTask
                    openDialog = true
                    false
                } else {
                    true
                }
            }, positionalThreshold = { totalDistance: Float -> totalDistance * 0.3f })

            if (swipedTaskState.value != null) {
                DisplayAlertDialog(
                    title = stringResource(id = R.string.delete_task, swipedTaskState.value?.title ?: ""),
                    message = stringResource(id = R.string.delete_task_confirmation, swipedTaskState.value?.title ?: ""),
                    openDialog = true,
                    closeDialog = { swipedTaskState.value = null },
                    onYesClicked = {
                        swipedTaskState.value?.let { onSwipeToDelete(Action.DELETE, it) }
                        swipedTaskState.value = null
                    }
                )
            }

            val degrees by animateFloatAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) {
                    0f
                } else {
                    -45f
                }, label = "Delete"
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = { RedBackground(degrees = degrees) },
                content = {
                    TaskItem(toDoTask = selectedTask, navigateTaskScreen = navigateTaskScreen)
                },
                enableDismissFromStartToEnd = false,
            )
        }

    }
}


@Composable
fun RedBackground(degrees: Float) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
            .padding(horizontal = LARGEST_PADDING), contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = Color.White,
            modifier = Modifier.rotate(degrees)
        )
    }

}


@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateTaskScreen: (taskId: Int) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = { navigateTaskScreen(toDoTask.id) }) {
        Column(
            modifier = Modifier
                .padding(LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = toDoTask.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(8f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
                        drawCircle(color = toDoTask.priority.color)
                    }
                }
            }
            Text(
                text = toDoTask.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}