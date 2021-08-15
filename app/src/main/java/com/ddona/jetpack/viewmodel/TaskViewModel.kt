package com.ddona.jetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ddona.jetpack.db.TaskRepository
import com.ddona.jetpack.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val task = taskRepository.getAllTaskWithLiveData()

    val tasksPaging = Pager(
        PagingConfig(pageSize = 20)
    ) {
        taskRepository.getTasksWithPaging()
    }.flow.cachedIn(viewModelScope)

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