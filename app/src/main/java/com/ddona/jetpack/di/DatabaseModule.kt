package com.ddona.jetpack.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ddona.jetpack.db.TaskDao
import com.ddona.jetpack.db.TaskDatabase
import com.ddona.jetpack.model.Task
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(
        application: Application,
        scope: CoroutineScope,
        taskDaoProvider: Provider<TaskDao>
    ): TaskDatabase {

        return Room.databaseBuilder(
            application.applicationContext,
            TaskDatabase::class.java,
            "task_database"
        ).fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    //the same way: https://dev.to/anandpushkar088/experimenting-with-dagger-hilt-3e80
                    scope.launch {
                        populateDatabase(taskDaoProvider.get())
                    }
                }
            })
            .build()
    }

    private suspend fun populateDatabase(taskDao: TaskDao) {
        taskDao.insertTask(Task(1, "Task 1", "Description 1", System.currentTimeMillis()))
        taskDao.insertTask(Task(2, "Task 2", "Description 2", System.currentTimeMillis()))
        taskDao.insertTask(Task(3, "Task 3", "Description 3", System.currentTimeMillis()))
    }

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.taskDao()
    }

}