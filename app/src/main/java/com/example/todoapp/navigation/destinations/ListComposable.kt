package com.example.todoapp.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoapp.ui.screens.list.ListScreens
import com.example.todoapp.ui.viewModels.SharedViewModels
import com.example.todoapp.utils.Action
import com.example.todoapp.utils.Constants.LIST_ARGUMENTS_KEY
import com.example.todoapp.utils.Constants.LIST_SCREEN
import com.example.todoapp.utils.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskID: Int) -> Unit, sharedViewModels: SharedViewModels
) {

    composable(
        route = LIST_SCREEN, arguments = listOf(navArgument(LIST_ARGUMENTS_KEY) {
            type = NavType.StringType
        })
    ) { navBackStack ->

        val action = navBackStack.arguments?.getString(LIST_ARGUMENTS_KEY).toAction()

        var myAction by rememberSaveable {
            mutableStateOf(Action.NO_ACTION)
        }

        LaunchedEffect(key1 = myAction) {
            if (action != myAction) {
                myAction = action
                sharedViewModels.updateAction(newAction = action)
            }
        }

        val databaseAction = sharedViewModels.action

        ListScreens(
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModels = sharedViewModels,
            action = databaseAction
        )

    }

}