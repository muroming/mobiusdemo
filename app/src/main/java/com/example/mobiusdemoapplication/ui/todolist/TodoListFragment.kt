package com.example.mobiusdemoapplication.ui.todolist

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiusdemoapplication.R
import com.example.mobiusdemoapplication.data.ScootersRepository
import com.example.mobiusdemoapplication.databinding.FragmentTodoListBinding
import com.example.mobiusdemoapplication.domain.TodoItem
import com.example.mobiusdemoapplication.ui.todoedit.TodoEditBottomSheet
import com.google.android.material.snackbar.Snackbar

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoListViewModel = TodoListViewModel(ScootersRepository())
    private val adapter = TodoListAdapter(
        onItemClicked = viewModel::onTodoItemClicked,
        onItemChecked = { id, isFinished -> viewModel.onTodoItemChecked(id, isFinished) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener(
            TodoEditBottomSheet.EDIT_RESULT,
            this
        ) { _, res ->
            val item = res.getSerializable(TodoEditBottomSheet.EDIT_RESULT) as TodoItem
            viewModel.updateItem(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodoListBinding.bind(view).apply {
            rvTodoList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@TodoListFragment.adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        with(viewModel) {
            todoItems.observe(viewLifecycleOwner) { it?.let(::updateTodos) }
            isLoading.observe(viewLifecycleOwner) { binding.loadingGroup.isVisible = it }
            snackText.observe(viewLifecycleOwner) { showSnack(it) }
            editingTodo.observe(viewLifecycleOwner) { openBottomSheet(it) }
        }
    }

    private fun updateTodos(todos: List<TodoItem>) {
        todos.map {
            val dueDate = when {
                DateUtils.isToday(it.dueDate.time) -> DueDate("Today",R.color.due_today)
                DateUtils.isToday(it.dueDate.time + (1000 * 60 * 60 * 24)) -> DueDate("Tomorrow", R.color.due_tomorrow)
                else -> null
            }
            UiTodoItem(
                id = it.id,
                title = it.title,
                type = it.type,
                dueDate = dueDate,
                isFinished = it.isFinished
            )
        }.let(adapter::submitList)
    }

    private fun showSnack(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }

    private fun openBottomSheet(item: TodoItem) {
        TodoEditBottomSheet.instance(item).show(childFragmentManager, null)
    }
}