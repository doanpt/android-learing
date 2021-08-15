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
        for (i in 0..10000) {
            taskDao.insertTask(Task(i, "Task $i", "Description $i", System.currentTimeMillis()))
        }
    }

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.taskDao()
    }

}