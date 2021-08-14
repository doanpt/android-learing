package com.ddona.jetpack.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "task_name")
    var title: String,
    @ColumnInfo(name = "task_description")
    var description: String,
    @ColumnInfo(name = "task_deadline")
    var deadline: Long
    //You can use @Ignore to ignore kotlin field to room
)
