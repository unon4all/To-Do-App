package com.example.todoapp.ui.viewModels

import androidx.compose.runtime.MutableState
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

@HiltViewModel
class SharedViewModels @Inject constructor(
    private val repository: ToDoRepository, private val dataStoreRepository: DataStoreRepository
) : ViewModel() {


    var action by mutableStateOf(NO_ACTION)
        private set

    var id by mutableIntStateOf(0)
        private set

    var title by mutableStateOf("")
        private set


    var desc by mutableStateOf("")
        private set

    var priority by mutableStateOf(Priority.LOW)
        private set


    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    private val _searchTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTask: StateFlow<RequestState<List<ToDoTask>>> = _searchTask

    private val _allTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTask: StateFlow<RequestState<List<ToDoTask>>> = _allTask

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    init {
        getAllTask()
        readSortState()
    }

    private fun getAllTask() {

        _allTask.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository.getAllTask.collect {
                    _allTask.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTask.value = RequestState.Error(error = e)
        }

    }

    fun searchDatabase(searchQuery: String) {

        _searchTask.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%").collect { searchedTask ->
                    _searchTask.value = RequestState.Success(searchedTask)
                }
            }
        } catch (e: Exception) {
            _searchTask.value = RequestState.Error(error = e)
        }

        searchAppBarState = SearchAppBarState.TRIGGERED

    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int) {

        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }


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


    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }


    fun validateFields(): Boolean {

        return title.isNotEmpty() && desc.isNotEmpty()

    }


    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title, description = desc, priority = priority
            )

            repository.addTask(toDoTask = toDoTask)
        }

        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id, title = title, description = desc, priority = priority
            )

            repository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteSingleTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id, title = title, description = desc, priority = priority
            )

            repository.deleteTask(toDoTask = toDoTask)
        }
    }

    fun handleDatabaseActions(action: Action) {

        when (action) {
            ADD -> addTask()
            UPDATE -> updateTask()
            DELETE -> deleteSingleTask()
            DELETE_ALL -> deleteAll()
            UNDO -> addTask()
            NO_ACTION -> {

            }
        }

        this.action = NO_ACTION

    }


    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    val lowPriorityTask: StateFlow<List<ToDoTask>> = repository.sortByLowPriority.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList()
    )

    val highPriorityTask: StateFlow<List<ToDoTask>> = repository.sortByHighPriority.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList()
    )


    private fun readSortState() {
        _sortState.value = RequestState.Loading

        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState.map {
                    Priority.valueOf(it)
                }.collect {
                    _sortState.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(error = e)
        }
    }

    fun updateAction(newAction: Action) {
        action = newAction
    }

    fun updateDescription(newDesc: String) {
        desc = newDesc
    }

    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    fun updateSearchAppBarState(newState: SearchAppBarState) {
        searchAppBarState = newState
    }

    fun updateSearchTextState(newText: String) {
        searchTextState = newText
    }

}