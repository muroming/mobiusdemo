package com.example.mobiusdemoapplication.ui.todoedit

import android.widget.CalendarView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mobiusdemoapplication.R
import com.example.mobiusdemoapplication.domain.TodoItem
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TodoEditScreen(
    todoItem: TodoItem,
    onSaveClicked: (TodoItem) -> Unit,
) {
    val viewModel = rememberSaveable { TodoEditFeature(todoItem) }
    val state = viewModel.state
    val action = viewModel::accept

    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
        TextField(
            value = state.title,
            onValueChange = { action(TodoEditFeature.Msg.OnTitleInput(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = { action(TodoEditFeature.Msg.OnChangeDateClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) { Text(text = state.date) }

        Button(onClick = { onSaveClicked(viewModel.todoItem) }) {
            Text(text = "Save")
        }
    }

    if (state.pickerDate != null) {
        DatePicker(pickerDate = state.pickerDate, action)
    }
}

@Composable
fun DatePicker(
    pickerDate: TodoEditFeature.State.PickerDate,
    action: (TodoEditFeature.Msg) -> Unit,
) {
    Dialog(
        onDismissRequest = { action(TodoEditFeature.Msg.CloseDateDialog) },
    ) {
        AndroidView(
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colors.surface),
            factory = { context ->
                CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom)).apply {
                    date = pickerDate.date
                    setOnDateChangeListener { _, year, month, day ->
                        action(TodoEditFeature.Msg.OnDateSelected(day, month + 1, year))
                    }
                }
            },
            update = {}
        )
    }
}