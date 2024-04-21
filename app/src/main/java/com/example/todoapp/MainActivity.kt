package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.navigation.SetupNavigation
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.example.todoapp.viewModels.SharedViewModels
import dagger.hilt.android.AndroidEntryPoint

// Annotating with @AndroidEntryPoint enables Hilt for this Activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Declaring a NavHostController to manage navigation
    private lateinit var navController: NavHostController

    // Declaring an instance of SharedViewModels using Hilt's viewModels delegate
    private val sharedViewModels: SharedViewModels by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Installing splash screen if available
        installSplashScreen()

        // Setting the window insets behavior
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Setting the content of the activity using Jetpack Compose
        setContent {
            // Applying the theme defined in ToDoAppTheme
            ToDoAppTheme {
                // Remembering the navigation controller
                navController = rememberNavController()

                // Setting up navigation with the remembered navController and sharedViewModels
                SetupNavigation(navController = navController, sharedViewModels = sharedViewModels)
            }
        }
    }
}