package com.example.mobiusdemoapplication.ui.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiusdemoapplication.databinding.ItemTodoBinding

internal class TodoListAdapter(
    private val onItemClicked: (String) -> Unit,
    private val onItemChecked: (String, Boolean) -> Unit
) : ListAdapter<UiTodoItem, TodoListAdapter.TodoViewHolder>(UiTodoItemsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TodoViewHolder(
        ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) =
        holder.bind(getItem(position))

    internal inner class TodoViewHolder(
        private val binding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UiTodoItem) {
            with(binding) {
                root.setOnClickListener { onItemClicked(item.id) }
                rbIsDone.apply{
                    setOnCheckedChangeListener(null)
                    isChecked = item.isFinished
                    setOnCheckedChangeListener { _, isChecked -> onItemChecked(item.id, isChecked) }
                }
                tvTitle.text = item.title
                ivIcon.setImageResource(item.type.itemIcon)

                val dueColor = item.dueDate?.color?.let { ContextCompat.getColor(root.context, it) }
                ivCalendar.apply {
                    isVisible = item.dueDate != null
                    dueColor?.let(::setColorFilter)
                }
                tvDue.apply {
                    isVisible = item.dueDate != null
                    text = item.dueDate?.text
                    dueColor?.let(::setTextColor)
                }
            }
        }
    }
}

private class UiTodoItemsDiffCallback : DiffUtil.ItemCallback<UiTodoItem>() {
    override fun areItemsTheSame(oldItem: UiTodoItem, newItem: UiTodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UiTodoItem, newItem: UiTodoItem): Boolean =
        oldItem == newItem
}