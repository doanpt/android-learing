package com.ddona.jetpack.db.school.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ddona.jetpack.db.school.School
import com.ddona.jetpack.db.school.Student

data class SchoolAndStudents(
    @Embedded val school: School,

    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val students: List<Student>
)