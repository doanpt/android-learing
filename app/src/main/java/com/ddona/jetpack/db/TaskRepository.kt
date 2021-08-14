package com.ddona.jetpack.db

import androidx.lifecycle.LiveData
import com.ddona.jetpack.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteById(taskId: Int) {
        taskDao.deleteById(taskId)
    }

    suspend fun deleteAllTask() {
        taskDao.deleteAllTask()
    }

    suspend fun getAllTask(): List<Task> {
        return taskDao.getAllTask()
    }

    fun getAllTaskWithLiveData(): LiveData<List<Task>> {
        return taskDao.getAllTaskWithLiveData()
    }

    fun getAllTaskWithFlow(): Flow<List<Task>> {
        return taskDao.getAllTaskWithFlow()
    }
}