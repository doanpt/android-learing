package com.example.demo_spring.service

import com.example.demo_spring.model.Student
import com.example.demo_spring.repository.StudentRepositoryJDBC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudentServiceJDBC {

    @Autowired
    private lateinit var studentRepositoryJDBC: StudentRepositoryJDBC

    fun getAllStudents(): List<Student> {
        return studentRepositoryJDBC.getAllStudents()
    }

    fun getStudentById(id: Int): Student? {
        return studentRepositoryJDBC.getStudentById(id)
    }

    fun deleteStudentById(id: Int): Int {
        return studentRepositoryJDBC.deleteStudentById(id)
    }

    fun updateStudent(student: Student): Int {
        return studentRepositoryJDBC.updateStudent(student)
    }

    fun insertStudent(student: Student): Int {
        return studentRepositoryJDBC.insertStudent(student)
    }
}