package com.example.mobiusdemoapplication.ui.todoedit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.mobiusdemoapplication.R
import com.example.mobiusdemoapplication.databinding.FragmentTodoEditBinding
import com.example.mobiusdemoapplication.domain.TodoItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class TodoEditBottomSheet : BottomSheetDialogFragment() {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val todoItem: TodoItem by lazy {
        requireArguments().getSerializable(TODO_ITEM) as TodoItem
    }
    private var updatedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_todo_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentTodoEditBinding.bind(view).apply { fillForm(todoItem) }
    }

    private fun FragmentTodoEditBinding.fillForm(todoItem: TodoItem) {
        etTitle.setText(todoItem.title)
        vDate.apply {
            text = dateFormatter.format(todoItem.dueDate)
            setOnClickListener { openDatePicker(todoItem.dueDate) }
        }
        vSave.setOnClickListener { saveItem() }
    }

    private fun FragmentTodoEditBinding.openDatePicker(date: Date) {
        val parsedDate = dateFormatter.format(date).split(".")
        DatePickerDialog(
            requireContext(),
            { _, year, month, day -> updateDate(year, month, day) },
            parsedDate[2].toInt(),
            parsedDate[1].toInt(),
            parsedDate[0].toInt()
        ).show()
    }

    private fun FragmentTodoEditBinding.updateDate(year: Int, month: Int, day: Int) {
        val newDate = "$day.$month.$year"
        updatedDate = dateFormatter.parse(newDate)
        vDate.text = newDate
    }

    private fun FragmentTodoEditBinding.saveItem() {
        val updatedItem = todoItem.copy(
            title = etTitle.text.toString(),
            dueDate = updatedDate ?: todoItem.dueDate
        )
        setFragmentResult(EDIT_RESULT, bundleOf(EDIT_RESULT to updatedItem))
        dismiss()
    }

    companion object {
        const val EDIT_RESULT = "edit-result"
        private const val TODO_ITEM = "todoitem"

        fun instance(item: TodoItem) = TodoEditBottomSheet().apply {
            arguments = bundleOf(TODO_ITEM to item)
        }
    }
}