package com.example.mobiusdemoapplication.ui.todolist

import androidx.annotation.ColorRes
import com.example.mobiusdemoapplication.domain.TodoItem

data class UiTodoItem(
    val id: String,
    val title: String,
    val type: TodoItemType,
    val dueDate: DueDate?,
    val isFinished: Boolean,
    val source: TodoItem
)

class DueDate(
    val text: String,
    @ColorRes val color: Int
)