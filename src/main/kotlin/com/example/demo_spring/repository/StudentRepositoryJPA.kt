package com.example.demo_spring.repository

import com.example.demo_spring.model.Student
import com.example.demo_spring.model.StudentJPA
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepositoryJPA : CrudRepository<StudentJPA, Int> {
    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
    fun findStudentJPAByName(name: String): Student
    fun findStudentJPASByName(name: String): List<StudentJPA>
    fun findStudentJPASByScoreGreaterThan(score: Float): List<StudentJPA>
}