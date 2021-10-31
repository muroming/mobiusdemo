package com.example.mobiusdemoapplication.data

import com.example.mobiusdemoapplication.domain.TodoItem
import com.example.mobiusdemoapplication.ui.todolist.TodoItemType
import kotlinx.coroutines.delay
import java.util.*
import kotlin.random.Random

class ScootersRepository {
    suspend fun loadUserScooters(): List<TodoItem> {
        delay(300L)
        return listOf(
            TodoItem(
                "1",
                "Ninebot Max",
                TodoItemType.CHORES,
                Date(),
                false
            ),
            TodoItem(
                "2",
                "Xiaomi Electric 15",
                TodoItemType.MUSIC,
                Date(),
                true
            )
        )
    }
}