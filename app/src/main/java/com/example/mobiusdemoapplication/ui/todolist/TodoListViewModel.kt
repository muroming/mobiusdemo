package com.example.mobiusdemoapplication.ui.todolist

import com.example.mobiusdemoapplication.data.TodosRepository
import com.example.mobiusdemoapplication.domain.TodoItem
import com.example.mobiusdemoapplication.tea.TeaEffectHandler
import com.example.mobiusdemoapplication.tea.TeaFeatureHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private typealias ReducerResult = Pair<TodoListViewModel.State, Set<TodoListViewModel.Effect>>

@HiltViewModel
class TodoListViewModel @Inject constructor(
    todosRepository: TodosRepository,
) : TeaFeatureHolder<TodoListViewModel.Msg, TodoListViewModel.State, TodoListViewModel.Effect>(
    initState = State.Loading,
    initEffects = setOf(Effect.LoadTodoList),
    Effect::class to TodoListEffectHandler(todosRepository)
) {

    sealed class State {
        object Loading : State()
        data class Loaded(
            val todoItems: List<TodoItem>,
            val editItem: TodoItem? = null
        ) : State()

        val loaded get() = this as Loaded
    }

    sealed class Msg {
        class OnItemsLoaded(val items: List<TodoItem>) : Msg()
        object ReloadItems : Msg()

        class UpdateItem(val item: TodoItem) : Msg()
        class OnTodoItemClicked(val item: TodoItem) : Msg()
        object CloseItemEditor : Msg()
        class OnTodoItemChecked(val itemId: String, val isChecked: Boolean) : Msg()
    }

    sealed class Effect {
        object LoadTodoList : Effect()
    }

    override fun reduce(msg: Msg, state: State): ReducerResult = when (msg) {
        is Msg.OnItemsLoaded -> State.Loaded(msg.items) to emptySet()
        is Msg.ReloadItems -> State.Loading to setOf(Effect.LoadTodoList)
        is Msg.OnTodoItemChecked -> onTodoItemChecked(state.loaded, msg.itemId, msg.isChecked)
        is Msg.OnTodoItemClicked -> onTodoItemClicked(state.loaded, msg.item)
        is Msg.CloseItemEditor -> state.loaded.copy(editItem = null) to emptySet()
        is Msg.UpdateItem -> updateItem(state.loaded, msg.item)
    }

    private fun updateItem(state: State.Loaded, item: TodoItem): ReducerResult {
        return state.copy(
            todoItems = state.todoItems.map { it.takeIf { it.id != item.id } ?: item },
            editItem = null
        ) to emptySet()
    }

    private fun onTodoItemClicked(state: State.Loaded, item: TodoItem): ReducerResult {
        return state.copy(editItem = item) to emptySet()
    }

    private fun onTodoItemChecked(
        state: State.Loaded,
        itemId: String,
        isDone: Boolean
    ): ReducerResult = state.copy(
        todoItems = state.todoItems.map { item ->
            if (item.id == itemId) {
                item.copy(isFinished = isDone)
            } else {
                item
            }
        }
    ) to emptySet()
}

private class TodoListEffectHandler(
    private val repository: TodosRepository
) : TeaEffectHandler<TodoListViewModel.Effect, TodoListViewModel.Msg> {
    override suspend fun execute(
        eff: TodoListViewModel.Effect,
        consumer: (TodoListViewModel.Msg) -> Unit
    ) {
        when (eff) {
            TodoListViewModel.Effect.LoadTodoList -> loadTodos()
        }.let(consumer)
    }

    private suspend fun loadTodos() = repository.loadUserTodos().let {
        TodoListViewModel.Msg.OnItemsLoaded(it)
    }
}