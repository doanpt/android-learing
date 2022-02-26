package com.example.demo_spring.repository

import com.example.demo_spring.model.Student
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryJDBC {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    val studentMappings = RowMapper<Student> { rs, _ ->
        Student(
                rs.getInt("col_id"),
                rs.getString("col_name"),
                rs.getString("col_address"),
                rs.getString("col_class"),
                rs.getFloat("col_point")
        )
    }

    fun getAllStudents(): List<Student> {
        return jdbcTemplate.query("SELECT * FROM tb_student", studentMappings)
    }

    fun getStudentById(id: Int): Student? {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM tb_student WHERE col_id=?",
                arrayOf(id), studentMappings
        )
    }

    fun deleteStudentById(id: Int): Int {
        return jdbcTemplate.update("DELETE FROM tb_student WHERE col_id=?", id)
    }

    fun updateStudent(student: Student): Int {
        return jdbcTemplate.update(
                "UPDATE tb_student set col_name=?, col_address=?, col_class=?, col_point=? WHERE col_id=?",
                student.name, student.address, student.className, student.score, student.id
        )
    }

    fun insertStudent(student: Student): Int {
        return jdbcTemplate.update(
                "INSERT INTO tb_student(col_name, col_address, col_class,col_point) values(?,?,?,?)",
                student.name, student.address, student.className, student.score
        )
    }
}