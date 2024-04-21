package com.example.todoapp.navigation.destinations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoapp.ui.screens.task.TaskScreen
import com.example.todoapp.viewModels.SharedViewModels
import com.example.todoapp.utils.Action
import com.example.todoapp.utils.Constants.TASK_ARGUMENTS_KEY
import com.example.todoapp.utils.Constants.TASK_SCREEN

fun NavGraphBuilder.taskComposable(
    sharedViewModels: SharedViewModels,
    navigateToListScreen: (Action) -> Unit,
) {

    composable(
        route = TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARGUMENTS_KEY) {
            type = NavType.IntType
        }),
        enterTransition = {
            slideInHorizontally(animationSpec = tween(durationMillis = 400),
                initialOffsetX = { fullWidth -> -fullWidth })
        }
    ) { navBackStackEntry ->

        val taskId = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENTS_KEY)

        LaunchedEffect(key1 = taskId) {
            sharedViewModels.getSelectedTask(taskId = taskId)
        }

        val selectedTask by sharedViewModels.selectedTask.collectAsState()

        LaunchedEffect(key1 = selectedTask) {
            if (selectedTask != null || taskId == -1) {
                sharedViewModels.updateTaskField(selectedTask = selectedTask)
            }
        }

        TaskScreen(
            sharedViewModels = sharedViewModels,
            navigateToListScreen = navigateToListScreen,
            selectedTask = selectedTask
        )
    }

}