package com.example.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.todoapp.navigation.destinations.listComposable
import com.example.todoapp.navigation.destinations.taskComposable
import com.example.todoapp.viewModels.SharedViewModels
import com.example.todoapp.utils.Constants.LIST_SCREEN



/**
 * Function to set up navigation within the application.
 *
 * @param navController The navigation controller responsible for managing navigation within the app.
 * @param sharedViewModels A class containing shared ViewModels between composables.
 */
@Composable
fun SetupNavigation(navController: NavHostController, sharedViewModels: SharedViewModels) {

    // Create an instance of the Screens class to hold references to screen destinations
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    // Define the navigation graph using NavHost composable
    NavHost(navController = navController, startDestination = LIST_SCREEN) {

        // Navigate to the list screen
        listComposable(navigateToTaskScreen = screen.list, sharedViewModels = sharedViewModels)

        // Navigate to the task screen
        taskComposable(navigateToListScreen = screen.task, sharedViewModels = sharedViewModels)
    }
}
