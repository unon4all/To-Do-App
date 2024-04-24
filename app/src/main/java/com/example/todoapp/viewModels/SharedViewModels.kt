package com.example.todoapp.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.data.repository.DataStoreRepository
import com.example.todoapp.data.repository.ToDoRepository
import com.example.todoapp.utils.Action
import com.example.todoapp.utils.Action.*
import com.example.todoapp.utils.Constants.MAX_TITLE_LENGTH
import com.example.todoapp.utils.RequestState
import com.example.todoapp.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel class for shared data and actions across fragments
@HiltViewModel
class SharedViewModels @Inject constructor(
    private val repository: ToDoRepository, // Repository for ToDo tasks
    private val dataStoreRepository: DataStoreRepository // Repository for data store operations
) : ViewModel() {

    // State variables using mutableStateOf for reactive UI updates
    var action by mutableStateOf(NO_ACTION) // Current action being performed
        private set

    var id by mutableIntStateOf(0) // ID of the selected task
        private set

    var title by mutableStateOf("") // Title of the task
        private set

    var desc by mutableStateOf("") // Description of the task
        private set

    var priority by mutableStateOf(Priority.LOW) // Priority of the task
        private set

    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED) // State of the search app bar
        private set

    var searchTextState by mutableStateOf("") // Text entered in the search bar
        private set

    // StateFlow for searched tasks
    private val _searchTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTask: StateFlow<RequestState<List<ToDoTask>>> = _searchTask

    // StateFlow for all tasks
    private val _allTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTask: StateFlow<RequestState<List<ToDoTask>>> = _allTask

    // StateFlow for sorting state
    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    // Initialization block
    init {
        getAllTask() // Load all tasks
        readSortState() // Read sorting state
    }

    // Function to retrieve all tasks
    private fun getAllTask() {
        _allTask.value = RequestState.Loading // Set loading state

        try {
            viewModelScope.launch {
                repository.getAllTask.collect { tasks ->
                    _allTask.value = RequestState.Success(tasks) // Set tasks on success
                }
            }
        } catch (e: Exception) {
            _allTask.value = RequestState.Error(error = e) // Set error state
        }
    }

    // Function to search tasks in the database
    fun searchDatabase(searchQuery: String) {
        _searchTask.value = RequestState.Loading // Set loading state

        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%").collect { searchedTask ->
                    _searchTask.value = RequestState.Success(searchedTask) // Set searched tasks on success
                }
            }
        } catch (e: Exception) {
            _searchTask.value = RequestState.Error(error = e) // Set error state
        }

        searchAppBarState = SearchAppBarState.TRIGGERED // Set search app bar state
    }

    // StateFlow for the selected task
    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    // Function to get a selected task
    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task // Set selected task
            }
        }
    }

    // Function to update task fields
    fun updateTaskField(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id = selectedTask.id
            title = selectedTask.title
            desc = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            desc = ""
            priority = Priority.LOW
        }
    }

    // Function to update title with validation
    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }

    // Function to validate task fields
    fun validateFields(): Boolean {
        return title.isNotEmpty() && desc.isNotEmpty()
    }

    // Function to add a task
    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title, description = desc, priority = priority
            )
            repository.addTask(toDoTask = toDoTask) // Add task to the repository
        }

        searchAppBarState = SearchAppBarState.CLOSED // Close search app bar after adding task
    }

    // Function to update a task
    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id, title = title, description = desc, priority = priority
            )
            repository.updateTask(toDoTask = toDoTask) // Update task in the repository
        }
    }

    // Function to delete a single task
    private fun deleteSingleTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id, title = title, description = desc, priority = priority
            )
            repository.deleteTask(toDoTask = toDoTask) // Delete task from the repository
        }
    }

    // Function to handle various database actions
    fun handleDatabaseActions(action: Action) {
        when (action) {
            ADD -> addTask()
            UPDATE -> updateTask()
            DELETE -> deleteSingleTask()
            DELETE_ALL -> deleteAll()
            UNDO -> addTask()
            NO_ACTION -> {
                // No action needed
            }
        }

        this.action = NO_ACTION // Reset action after handling
    }

    // Function to delete all tasks

    // Comment add

    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask() // Delete all tasks from the repository
        }
    }

    // Function to persist sorting state
    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority) // Persist sorting state in data store
        }
    }

    // StateFlow for low priority tasks
    val lowPriorityTask: StateFlow<List<ToDoTask>> = repository.sortByLowPriority.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList()
    )

    // StateFlow for high priority tasks
    val highPriorityTask: StateFlow<List<ToDoTask>> = repository.sortByHighPriority.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList()
    )

    // Function to read sorting state
    private fun readSortState() {
        _sortState.value = RequestState.Loading // Set loading state

        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState.map {
                    Priority.valueOf(it) // Map stored value to Priority enum
                }.collect {
                    _sortState.value = RequestState.Success(it) // Set sorting state on success
                }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(error = e) // Set error state
        }
    }

    // Function to update current action
    fun updateAction(newAction: Action) {
        action = newAction
    }

    // Function to update task description
    fun updateDescription(newDesc: String) {
        desc = newDesc
    }

    // Function to update task priority
    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    // Function to update search app bar state
    fun updateSearchAppBarState(newState: SearchAppBarState) {
        searchAppBarState = newState
    }

    // Function to update search text state
    fun updateSearchTextState(newText: String) {
        searchTextState = newText
    }
}