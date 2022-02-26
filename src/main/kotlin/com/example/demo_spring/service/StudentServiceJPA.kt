package com.example.demo_spring.service

import com.example.demo_spring.model.StudentJPA
import com.example.demo_spring.repository.StudentRepositoryJPA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentServiceJPA {
    @Autowired
    private lateinit var studentRepositoryJPA: StudentRepositoryJPA

    fun getAllStudents(): List<StudentJPA> {
        return studentRepositoryJPA.findAll().toList()
    }

    fun getStudentById(id: Int): Optional<StudentJPA> {
        return studentRepositoryJPA.findById(id)
    }

    fun deleteStudentById(id: Int) {
        return studentRepositoryJPA.deleteById(id)
    }

    fun insertStudent(student: StudentJPA): StudentJPA {
        return studentRepositoryJPA.save(student)
    }

    fun updateStudent(student: StudentJPA): StudentJPA {
        return studentRepositoryJPA.save(student)
    }
}