package com.example.mobiusdemoapplication.ui.todolist

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.mobiusdemoapplication.R
import com.example.mobiusdemoapplication.data.TodosRepository
import com.example.mobiusdemoapplication.domain.TodoItem

class TodoListFragment : Fragment() {

    private val viewModel: TodoListViewModel = TodoListViewModel(TodosRepository())

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            TodoItemsScreen(
                state = viewModel.state,
                action = viewModel::accept,
                itemsMapper = ::itemsMapper
            )
        }
    }

    private fun itemsMapper(todos: List<TodoItem>): List<UiTodoItem> = todos.map {
        val dueDate = when {
            DateUtils.isToday(it.dueDate.time) -> DueDate("Сегодня", R.color.due_today)
            DateUtils.isToday(it.dueDate.time - (1000 * 60 * 60 * 24)) -> DueDate(
                "Завтра",
                R.color.due_tomorrow
            )
            else -> null
        }
        UiTodoItem(
            id = it.id,
            title = it.title,
            type = it.type,
            dueDate = dueDate,
            isFinished = it.isFinished,
            source = it
        )
    }
}