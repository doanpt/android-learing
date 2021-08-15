package com.ddona.jetpack.db.school.relations

import androidx.room.*
import com.ddona.jetpack.db.school.Director
import com.ddona.jetpack.db.school.School
import com.ddona.jetpack.db.school.Student
import com.ddona.jetpack.db.school.Subject

@Dao
interface SchoolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: School)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirector(director: Director)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentSubjectCrossRef(crossRef: StudentSubjectCrossRef)

    @Transaction
    @Query("SELECT * FROM School WHERE schoolName = :schoolName")
    suspend fun getSchoolAndDirector(schoolName: String): List<SchoolAndDirection>

    @Transaction
    @Query("SELECT * FROM School WHERE schoolName = :schoolName")
    suspend fun getSchoolWithStudents(schoolName: String): List<SchoolAndStudents>

    @Transaction
    @Query("SELECT * FROM Subject WHERE subjectName = :subjectName")
    suspend fun getStudentsOfSubject(subjectName: String): List<SubjectWithStudents>

    @Transaction
    @Query("SELECT * FROM Student WHERE studentName = :studentName")
    suspend fun getSubjectsOfStudent(studentName: String): List<StudentWithSubjects>

}