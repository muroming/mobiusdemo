package com.example.mobiusdemoapplication.ui.todolist

import androidx.annotation.ColorRes

data class UiTodoItem(
    val id: String,
    val title: String,
    val type: TodoItemType,
    val dueDate: DueDate?,
    val isFinished: Boolean
)

class DueDate(
    val text: String,
    @ColorRes val color: Int
)