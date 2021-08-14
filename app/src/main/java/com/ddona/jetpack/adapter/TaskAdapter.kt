package com.ddona.jetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ddona.jetpack.databinding.ItemTaskBinding
import com.ddona.jetpack.diff.TaskDiffCallback
import com.ddona.jetpack.model.Task

class TaskAdapter() : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private val tasks = mutableListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun submit(taskList: List<Task>) {
        val diffCallback = TaskDiffCallback(tasks, taskList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.tasks.clear();
        this.tasks.addAll(taskList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.task = task
            binding.executePendingBindings()
        }
    }
}