package com.example.mobiusdemoapplication.ui.todolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobiusdemoapplication.domain.TodoItem
import com.example.mobiusdemoapplication.ui.todoedit.TodoEditScreen
import com.example.mobiusdemoapplication.R

@ExperimentalMaterialApi
@Composable
fun TodoItemsScreen(
    state: TodoListViewModel.State,
    action: (TodoListViewModel.Msg) -> Unit,
    itemsMapper: (List<TodoItem>) -> List<UiTodoItem>,
) {
    when (state) {
        is TodoListViewModel.State.Loading -> TodoListLoading()
        is TodoListViewModel.State.Loaded -> TodoListContent(
            itemsMapper(state.todoItems),
            state.editItem,
            action
        )
    }
}

@Composable
private fun TodoListLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun TodoListContent(
    items: List<UiTodoItem>,
    editItem: TodoItem?,
    action: (TodoListViewModel.Msg) -> Unit,
) {
    val modalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            action(TodoListViewModel.Msg.CloseItemEditor)
            false
        }
    )
    LaunchedEffect(editItem) {
        if (editItem != null) {
            modalState.show()
        } else {
            modalState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetContent = { TodoEditBottomSheet(editItem, action) }
    ) {
        LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)) {
            items(items) { item ->
                TodoItem(modifier = Modifier.padding(bottom = 8.dp), item, action)
            }
        }
    }
}

@Composable
private fun TodoEditBottomSheet(editItem: TodoItem?, action: (TodoListViewModel.Msg) -> Unit) {
    editItem?.let {
        TodoEditScreen(
            todoItem = editItem,
            onSaveClicked = { action(TodoListViewModel.Msg.UpdateItem(it)) }
        )
    } ?: Spacer(modifier = Modifier.size(1.dp))
}

@Composable
private fun TodoItem(
    modifier: Modifier,
    item: UiTodoItem,
    action: (TodoListViewModel.Msg) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                MutableInteractionSource(),
                rememberRipple()
            ) { action(TodoListViewModel.Msg.OnTodoItemClicked(item.source)) }
    ) {
        Checkbox(
            checked = item.isFinished,
            onCheckedChange = { action(TodoListViewModel.Msg.OnTodoItemChecked(item.id, it)) }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = item.title,
                maxLines = 1,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis
            )
            if (item.dueDate != null) {
                TodoDue(modifier = Modifier.padding(top = 4.dp), dueDate = item.dueDate)
            }
        }
        Icon(
            painter = painterResource(id = item.type.itemIcon),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun TodoDue(modifier: Modifier, dueDate: DueDate) {
    val dueColor = colorResource(id = dueDate.color)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = null,
            tint = dueColor,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = dueDate.text, color = dueColor)
    }
}