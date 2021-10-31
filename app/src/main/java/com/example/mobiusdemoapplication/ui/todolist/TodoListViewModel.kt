package com.example.mobiusdemoapplication.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiusdemoapplication.data.ScootersRepository
import com.example.mobiusdemoapplication.domain.TodoItem
import com.example.mobiusdemoapplication.tea.SingleLiveEvent
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val scootersRepository: ScootersRepository
) : ViewModel() {


    private val _todoItems = MutableLiveData<List<TodoItem>>()
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    private val _snackText = SingleLiveEvent<String>()
    val snackText: LiveData<String> = _snackText

    private val _editTodo = SingleLiveEvent<TodoItem>()
    val editingTodo: LiveData<TodoItem> = _editTodo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.value = true
            _todoItems.value = scootersRepository.loadUserScooters()
            _isLoading.value = false
        }
    }

    fun updateItem(item: TodoItem) {
        _todoItems.value = _todoItems.value?.map {
            it.takeIf { it.id != item.id } ?: item
        }
    }

    fun onTodoItemClicked(itemId: String) {
        val item = todoItems.value?.find { it.id == itemId } ?: return
        _editTodo.setValue(item)
    }

    fun onTodoItemChecked(itemId: String, isDone: Boolean) {
        val todoItems = _todoItems.value ?: return
        _todoItems.value = todoItems.map { item ->
            if (item.id == itemId) {
                item.copy(isFinished = isDone)
            } else {
                item
            }
        }
    }
}