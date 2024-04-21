package com.example.todoapp.data.repository

import com.example.todoapp.data.ToDoDao
import com.example.todoapp.data.models.ToDoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Repository for handling operations related to ToDoTask entities.
 * This repository acts as a mediator between the data source (ToDoDao) and the ViewModel.
 * @param toDoDao Data Access Object for ToDoTask entities.
 */
@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    // Flow representing a list of all tasks.
    val getAllTask: Flow<List<ToDoTask>> = toDoDao.getAllTask()

    // Flow representing a list of tasks sorted by low priority.
    val sortByLowPriority: Flow<List<ToDoTask>> = toDoDao.sortByLowPriority()

    // Flow representing a list of tasks sorted by high priority.
    val sortByHighPriority: Flow<List<ToDoTask>> = toDoDao.sortByHighPriority()

    /**
     * Retrieves a selected task by its ID.
     * @param taskId ID of the task to retrieve.
     * @return Flow representing the selected task.
     */
    fun getSelectedTask(taskId: Int): Flow<ToDoTask> {
        return toDoDao.getSelectedTask(taskId = taskId)
    }

    /**
     * Adds a new task to the database.
     * @param toDoTask The task to add.
     */
    suspend fun addTask(toDoTask: ToDoTask) {
        toDoDao.addTask(toDoTask = toDoTask)
    }

    /**
     * Updates an existing task in the database.
     * @param toDoTask The task to update.
     */
    suspend fun updateTask(toDoTask: ToDoTask) {
        toDoDao.updateTask(toDoTask = toDoTask)
    }

    /**
     * Deletes a task from the database.
     * @param toDoTask The task to delete.
     */
    suspend fun deleteTask(toDoTask: ToDoTask) {
        toDoDao.deleteTask(toDoTask = toDoTask)
    }

    /**
     * Deletes all tasks from the database.
     */
    suspend fun deleteAllTask() {
        toDoDao.deleteAllTask()
    }

    /**
     * Searches tasks in the database based on a search query.
     * @param searchQuery The query string to search for.
     * @return Flow representing the list of tasks matching the search query.
     */
    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> {
        return toDoDao.searchDataBase(searchQuery = searchQuery)
    }

}