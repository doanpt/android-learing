package com.ddona.jetpack.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ddona.jetpack.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//exportSchema true will let room export data base schema to a folder, it is good for history tracking
@Database(entities = [Task::class], exportSchema = false, version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
//        private var instance: TaskDatabase? = null
//
//        @Synchronized
//        fun getInstance(ctx: Context): TaskDatabase {
//            if (instance == null)
//                instance = Room.databaseBuilder(
//                    ctx.applicationContext, TaskDatabase::class.java,
//                    "task_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
//                    .build()
//
//            return instance!!
//
//        }

        @Volatile
        private var instance: TaskDatabase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): TaskDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context, scope).also { instance = it }
        }

        private fun buildDatabase(context: Context, scope: CoroutineScope): TaskDatabase {
            return Room.databaseBuilder(context, TaskDatabase::class.java, "task_database")
                .fallbackToDestructiveMigration()
                .addCallback(TaskDatabaseCallback(scope))
                .build()
        }

    }

    private class TaskDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            scope.launch {
                populateDatabase(instance!!)
            }
        }

        private suspend fun populateDatabase(db: TaskDatabase) {
            val taskDao = db.taskDao()
            taskDao.insertTask(Task(1, "Task 1", "Description 1", System.currentTimeMillis()))
            taskDao.insertTask(Task(2, "Task 2", "Description 2", System.currentTimeMillis()))
            taskDao.insertTask(Task(3, "Task 3", "Description 3", System.currentTimeMillis()))
        }
    }
}