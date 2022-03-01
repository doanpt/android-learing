package com.example.demo_spring.controller

import com.example.demo_spring.model.Student
import com.example.demo_spring.model.StudentJPA
import com.example.demo_spring.service.StudentServiceJPA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/student_jpa")
class StudentControllerJPA {
    @Autowired
    private lateinit var studentService: StudentServiceJPA

    @GetMapping("/")
    fun getAllStudents(): List<StudentJPA> {
        return studentService.getAllStudents()
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable("id") id: Int): ResponseEntity<StudentJPA> {
        return studentService.getStudentById(id).map { s ->
            ResponseEntity.ok(s)
        }.orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/name/{name}")
    fun findStudentJPAByName(@PathVariable name: String): Student {
        return studentService.findStudentJPAByName(name)
    }

    @GetMapping("/names/{name}")
    fun findStudentJPASByName(@PathVariable name: String): List<StudentJPA> {
        return studentService.findStudentJPASByName(name)
    }

    @GetMapping("/score/{score}")
    fun findStudentJPASByScoreGreaterThan(@PathVariable score: Float): List<StudentJPA> {
        return studentService.findStudentJPASByScoreGreaterThan(score)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteStudentById(@PathVariable("id") id: Int): ResponseEntity<Void> {
        return studentService.getStudentById(id).map { _ ->
            studentService.deleteStudentById(id)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(
            ResponseEntity.notFound().build()
        )
    }

    @PutMapping("/update")
    fun updateStudent(@RequestBody student: StudentJPA): ResponseEntity<StudentJPA> {
        return studentService.getStudentById(student.id).map {
            ResponseEntity.ok(studentService.updateStudent(student))
        }.orElse(
            ResponseEntity.notFound().build()
        )
    }

    @PostMapping("/add")
    fun insertStudent(@RequestBody student: StudentJPA): StudentJPA {
        return studentService.insertStudent(student)
    }
}