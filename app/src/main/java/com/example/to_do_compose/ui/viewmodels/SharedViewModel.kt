package com.example.to_do_compose.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do_compose.data.models.Priority
import com.example.to_do_compose.data.models.ToDoTask
import com.example.to_do_compose.data.repositories.ToDoRepository
import com.example.to_do_compose.utils.Action
import com.example.to_do_compose.utils.Constants.MAX_TITLE_LENGTH
import com.example.to_do_compose.utils.RequestState
import com.example.to_do_compose.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)


    val searchAppbarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    fun searchDatabase(searchQuery: String){
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect{searchedTasks->
                        _searchedTasks.value = RequestState.Success(searchedTasks)
                    }
            }
        }catch (e: Exception){
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppbarState.value = SearchAppBarState.TRIGGERED
    }

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    fun getAllTasks(){
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.getAllTasks.collect{
                    _allTasks.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _allTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int){
        viewModelScope.launch {
            toDoRepository.getSelectedTask(taskId = taskId).collect{task->
                _selectedTask.value = task
            }
        }
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.addTask(toDoTask)
        }
        searchAppbarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.updateTask(toDoTask)
        }
    }

    private fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            toDoRepository.deleteTask(toDoTask)
        }
    }

    private fun deleteAllTasks(){
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteTasks()
        }
    }

    fun handleDatabaseActions(action: Action){
        when(action){
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasks()
            }
            Action.UNDO -> {
                addTask()
            }
            else -> {}
        }
        this.action.value = Action.NO_ACTION
    }

    fun updateTaskFields(selectedTask: ToDoTask?){
        if(selectedTask != null){
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        }else{
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String){
        if(newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean{
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

}









