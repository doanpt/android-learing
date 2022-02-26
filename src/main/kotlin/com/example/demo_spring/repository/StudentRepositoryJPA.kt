package com.example.demo_spring.repository

import com.example.demo_spring.model.StudentJPA
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepositoryJPA: CrudRepository<StudentJPA, Int> {
}