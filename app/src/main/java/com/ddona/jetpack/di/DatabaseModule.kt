package com.ddona.jetpack.di

import android.app.Application
import androidx.room.Room
import com.ddona.jetpack.db.TaskDao
import com.ddona.jetpack.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(application: Application): TaskDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            TaskDatabase::class.java,
            "task_database"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.taskDao()
    }

}