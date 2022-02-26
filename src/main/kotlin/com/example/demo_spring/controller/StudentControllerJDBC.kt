package com.example.demo_spring.controller

import com.example.demo_spring.model.Student
import com.example.demo_spring.service.StudentServiceJDBC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/student")
class StudentControllerJDBC {
    @Autowired
    private lateinit var studentService: StudentServiceJDBC

    @GetMapping("/")
    fun getAllStudents(): List<Student> {
        return studentService.getAllStudents()
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable("id") id: Int): Student? {
        return studentService.getStudentById(id)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteStudentById(@PathVariable("id") id: Int): Int {
        return studentService.deleteStudentById(id)
    }

    @PutMapping("/update")
    fun updateStudent(@RequestBody student: Student): Int {
        return studentService.updateStudent(student)
    }

    @PostMapping("/add")
    fun insertStudent(@RequestBody student: Student): Int {
        return studentService.insertStudent(student)
    }
}