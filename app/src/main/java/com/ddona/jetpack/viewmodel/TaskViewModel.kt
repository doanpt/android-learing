package com.ddona.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddona.jetpack.db.TaskDatabase
import com.ddona.jetpack.db.TaskRepository
import com.ddona.jetpack.model.Task
import kotlinx.coroutines.flow.Flow

class TaskViewModel(private val application: Application) : ViewModel() {
    private val taskRepository =
        TaskRepository(
            TaskDatabase.getInstance(application.applicationContext, viewModelScope).taskDao()
        )

    val task = taskRepository.getAllTaskWithLiveData()

    suspend fun insertTask(task: Task) {
        //if you use this without create worker thread, Crash will be happen
        taskRepository.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    suspend fun deleteById(taskId: Int) {
        taskRepository.deleteById(taskId)
    }

    suspend fun deleteAllTask() {
        taskRepository.deleteAllTask()
    }

    suspend fun getAllTask(): List<Task> {
        //if you use this without create worker thread, Crash will be happen
        return taskRepository.getAllTask()
    }

    fun getAllTaskWithLiveData(): LiveData<List<Task>> {
        return taskRepository.getAllTaskWithLiveData()
    }

    fun getAllTaskWithFlow(): Flow<List<Task>> {
        return taskRepository.getAllTaskWithFlow()
    }
}