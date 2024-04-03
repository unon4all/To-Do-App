//package com.example.todoapp.navigation.destinations
//
//import android.annotation.SuppressLint
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.slideOutVertically
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.compose.composable
//import com.example.todoapp.ui.splash.SplashScreen
//import com.example.todoapp.utils.Constants
//
//@SuppressLint("NewApi")
//fun NavGraphBuilder.splashComposable(
//    navigateToTaskScreen: () -> Unit,
//) {
//
//    composable(route = Constants.SPLASH_SCREEN, exitTransition = {
//        slideOutVertically(
//            animationSpec = tween(500),
//            targetOffsetY = { fullHeight -> -fullHeight })
//    }) {
//        SplashScreen(navigateToListScreen = navigateToTaskScreen)
//    }
//
//}