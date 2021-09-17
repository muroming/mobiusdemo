package com.example.mobiusdemoapplication.data

import com.example.mobiusdemoapplication.domain.TodoItem
import com.example.mobiusdemoapplication.ui.todolist.TodoItemType
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject

class TodosRepository @Inject constructor() {
    suspend fun loadUserTodos(): List<TodoItem> {
        delay(500L)
        return listOf(
            TodoItem(
                "1",
                "Помыть посуду",
                TodoItemType.CHORES,
                Date(),
                false
            ),
            TodoItem(
                "2",
                "Купить скакалку",
                TodoItemType.FITNESS,
                Date(System.currentTimeMillis() + 1000 * 24 * 60 * 60),
                true
            ),
            TodoItem(
                "3",
                "Отправить заявку на фестиваль",
                TodoItemType.MUSIC,
                Date(System.currentTimeMillis() + 1000 * 72 * 60 * 60),
                false
            )
        )
    }
}