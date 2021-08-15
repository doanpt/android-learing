package com.ddona.jetpack.db.school.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ddona.jetpack.db.school.Director
import com.ddona.jetpack.db.school.School

data class SchoolAndDirection(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val director: Director
)