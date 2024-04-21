package com.example.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.models.ToDoTask

// Define the database annotation with the entities it contains, its version, and whether to export the schema.
@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    // Abstract function to provide access to the Data Access Object (DAO) for ToDoTask entities.
    abstract fun toDoDao(): ToDoDao
}