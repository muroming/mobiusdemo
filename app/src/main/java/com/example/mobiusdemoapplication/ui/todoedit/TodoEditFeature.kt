package com.example.mobiusdemoapplication.ui.todoedit

import android.os.Parcelable
import com.example.mobiusdemoapplication.domain.TodoItem
import com.example.mobiusdemoapplication.tea.TeaFeatureHolder
import com.example.mobiusdemoapplication.ui.todolist.TodoItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class TodoEditFeature(
    private val item: TodoItem,
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat(
        "dd.MM.yyyy",
        Locale.getDefault()
    )
) : TeaFeatureHolder<TodoEditFeature.Msg, TodoEditFeature.State, TodoEditFeature.Effect>(
    initState = State(item.title, item.type, dateFormatter.format(item.dueDate))
), Parcelable {
    data class State(
        val title: String,
        val todoItemType: TodoItemType,
        val date: String,
        val pickerDate: PickerDate? = null
    ) {
        class PickerDate(val date: Long)
    }

    sealed class Msg {
        class OnTitleInput(val title: String) : Msg()
        object CloseDateDialog : Msg()
        object OnChangeDateClicked : Msg()
        class OnDateSelected(val day: Int, val month: Int, val year: Int): Msg()
    }
    sealed class Effect

    val todoItem
        get() = TodoItem(
            item.id,
            state.title,
            state.todoItemType,
            dateFormatter.parse(state.date) ?: item.dueDate,
            item.isFinished
        )

    override fun reduce(msg: Msg, state: State): Pair<State, Set<Effect>> = when (msg) {
        is Msg.OnTitleInput -> state.copy(title = msg.title) to emptySet()
        is Msg.CloseDateDialog -> state.copy(pickerDate = null) to emptySet()
        is Msg.OnChangeDateClicked -> state.copy(
            pickerDate = State.PickerDate(dateFormatter.parse(state.date)!!.time)
        ) to emptySet()
        is Msg.OnDateSelected -> state.copy(
            date = "${msg.day}.${msg.month}.${msg.year}",
            pickerDate = null
        ) to emptySet()
    }
}