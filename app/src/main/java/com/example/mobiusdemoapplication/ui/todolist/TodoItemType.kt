package com.example.mobiusdemoapplication.ui.todolist

import androidx.annotation.DrawableRes
import com.example.mobiusdemoapplication.R

enum class TodoItemType(@DrawableRes val itemIcon: Int) {
    MUSIC(R.drawable.ic_music),
    HOMEWORK(R.drawable.ic_homework),
    CHORES(R.drawable.ic_chores),
    FITNESS(R.drawable.ic_fitness)
}