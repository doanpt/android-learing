package com.ddona.jetpack.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.ddona.jetpack.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task where id = :taskId")
    suspend fun deleteById(taskId: Int)

    @Query("DELETE FROM task")
    suspend fun deleteAllTask()

    //NOTE: If you return list, the query will be ran on UI Thread. you need to create a thread to run this query
    @Query("SELECT * FROM task")
    fun getAllTask(): List<Task>

    //NOTE: This query will run on background thread by room, don't need to create thread
    @Query("SELECT * FROM task")
    fun getAllTaskWithLiveData(): LiveData<List<Task>>

    @Query("SELECT * FROM task")
    fun getAllTaskWithFlow(): Flow<List<Task>>

    @Query("SELECT * FROM task")
    fun getTasksWithPaging(): PagingSource<Int, Task>
}