package com.example.todoapp.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.ui.viewModels.SharedViewModels
import com.example.todoapp.utils.Action

@Composable
fun TaskScreen(
    sharedViewModels: SharedViewModels,
    navigateToListScreen: (Action) -> Unit = {},
    selectedTask: ToDoTask?
) {
    val title: String = sharedViewModels.title
    val desc: String = sharedViewModels.desc
    val priority: Priority = sharedViewModels.priority

    val context = LocalContext.current

    BackHandler(onBack = { navigateToListScreen(Action.NO_ACTION) })

    Scaffold(topBar = {
        TaskAppBar(navigateToListScreen = { action ->
            if (action == Action.NO_ACTION) {
                navigateToListScreen(action)
            } else {
                if (sharedViewModels.validateFields()) {
                    navigateToListScreen(action)
                } else {
                    displayToast(context = context)
                }
            }

        }, selectedTask = selectedTask)
    }, content = {
        TaskContent(
            modifier = Modifier.padding(it),
            onTitleChange = { title -> sharedViewModels.updateTitle(title) },
            onDescriptionChange = { desc -> sharedViewModels.updateDescription(newDesc = desc) },
            onPrioritySelected = { priority -> sharedViewModels.updatePriority(newPriority = priority) },
            title = title,
            description = desc,
            priority = priority
        )
    })

}

fun displayToast(context: Context) {
    Toast.makeText(context, "Fields Empty", Toast.LENGTH_SHORT).show()
}


