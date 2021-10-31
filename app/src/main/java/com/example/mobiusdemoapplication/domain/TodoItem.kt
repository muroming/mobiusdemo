package com.example.mobiusdemoapplication.domain

import com.example.mobiusdemoapplication.ui.todolist.TodoItemType
import java.io.Serializable
import java.util.Date

data class TodoItem(
    val id: String,
    val title: String,
    val type: TodoItemType,
    val dueDate: Date,
    val isFinished: Boolean
) : Serializable