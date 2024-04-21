package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
// Annotation to indicate that this module should be installed in the SingletonComponent
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Annotation to specify that this function provides a singleton instance of the database
    @Singleton
    // Annotation to mark this function as providing a dependency
    @Provides
    // Function to provide an instance of the TodoDatabase
    fun provideDatabase(@ApplicationContext context: Context) =
        // Building a Room database instance
        Room.databaseBuilder(
            // Context parameter for building the database
            context = context,
            // Class reference for the TodoDatabase
            klass = TodoDatabase::class.java,
            // Name of the database
            name = DATABASE_NAME
        ).build()

    // Annotation to specify that this function provides a singleton instance of the DAO
    @Singleton
    // Annotation to mark this function as providing a dependency
    @Provides
    // Function to provide an instance of the ToDoDao
    fun provideDao(database: TodoDatabase) =
        // Accessing the DAO from the provided TodoDatabase instance
        database.toDoDao()
}