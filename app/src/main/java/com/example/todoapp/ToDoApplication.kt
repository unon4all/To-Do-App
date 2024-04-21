package com.example.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This is the main Application class for the ToDo application.
 * It is annotated with [@HiltAndroidApp] to enable Hilt for dependency injection.
 */
@HiltAndroidApp
class ToDoApplication : Application() {

    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using lazy initialization of state) since the time spent in this function directly impacts the performance of starting the first activity, service, or receiver in a process.
     */
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-wide resources or setup here
    }
}
