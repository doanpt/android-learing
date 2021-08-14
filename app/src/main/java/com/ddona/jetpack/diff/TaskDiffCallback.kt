package com.ddona.jetpack.diff

import androidx.recyclerview.widget.DiffUtil
import com.ddona.jetpack.model.Task

class TaskDiffCallback(
    private val oldTasks: List<Task>,
    private val newTasks: List<Task>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldTasks.size


    override fun getNewListSize(): Int = newTasks.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition].id == newTasks[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition] === newTasks[newItemPosition]
    }
}