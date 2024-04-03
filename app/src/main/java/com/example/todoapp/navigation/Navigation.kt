package com.example.todoapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.todoapp.navigation.destinations.listComposable
import com.example.todoapp.navigation.destinations.taskComposable
import com.example.todoapp.ui.viewModels.SharedViewModels
import com.example.todoapp.utils.Constants.LIST_SCREEN

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavigation(navController: NavHostController, sharedViewModels: SharedViewModels) {

    val screen = remember(navController) {
        Screens(navController = navController)
    }


    NavHost(navController = navController, startDestination = LIST_SCREEN) {
//        splashComposable(navigateToTaskScreen = screen.splash)
        listComposable(navigateToTaskScreen = screen.list, sharedViewModels = sharedViewModels)
        taskComposable(navigateToListScreen = screen.task, sharedViewModels = sharedViewModels)
    }
}