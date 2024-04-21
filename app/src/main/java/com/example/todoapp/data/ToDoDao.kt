package com.example.todoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) interface for ToDoTask entity.
 * Defines methods to interact with the ToDoTask database table.
 */
@Dao
interface ToDoDao {

    /**
     * Retrieves all tasks from the todo_table ordered by task ID in ascending order.
     * @return Flow emitting a list of all ToDoTask objects.
     */
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTask(): Flow<List<ToDoTask>>

    /**
     * Retrieves a specific task from the todo_table by its ID.
     * @param taskId ID of the task to retrieve.
     * @return Flow emitting the ToDoTask object with the specified ID.
     */
    @Query("SELECT * FROM todo_table WHERE id =:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    /**
     * Inserts a new task into the todo_table.
     * If there is a conflict with an existing task, it ignores the new task.
     * @param toDoTask The task to add to the database.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    /**
     * Updates an existing task in the todo_table.
     * @param toDoTask The updated task to replace the existing task.
     */
    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    /**
     * Deletes a task from the todo_table.
     * @param toDoTask The task to delete from the database.
     */
    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    /**
     * Deletes all tasks from the todo_table.
     */
    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTask()

    /**
     * Searches the todo_table for tasks with titles or descriptions matching the provided query.
     * @param searchQuery The query string to search for in task titles or descriptions.
     * @return Flow emitting a list of ToDoTask objects matching the search query.
     */
    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDataBase(searchQuery: String): Flow<List<ToDoTask>>

    /**
     * Retrieves tasks from the todo_table ordered by low priority first.
     * @return Flow emitting a list of ToDoTask objects sorted by low priority.
     */
    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    /**
     * Retrieves tasks from the todo_table ordered by high priority first.
     * @return Flow emitting a list of ToDoTask objects sorted by high priority.
     */
    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<ToDoTask>>
}
