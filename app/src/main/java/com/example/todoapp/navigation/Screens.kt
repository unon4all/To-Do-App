package com.example.todoapp.navigation

import androidx.navigation.NavHostController
import com.example.todoapp.utils.Action
import com.example.todoapp.utils.Constants.LIST_SCREEN


/**
 * This class represents a collection of screen navigation actions for a navigation controller.
 * It provides functions to navigate between different screens.
 * @param navController The navigation controller used for screen navigation.
 */
class Screens(navController: NavHostController) {

    /**
     * Navigate to the task details screen.
     * @param taskId The ID of the task to navigate to.
     */
    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }


    /**
     * Navigate to the task list screen with a specified action.
     * @param action The action to perform when navigating to the task list screen.
     */
    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
}