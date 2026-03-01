package com.pisroster.app.data.repository

import com.pisroster.app.data.dao.StudentDao
import com.pisroster.app.data.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

class StudentRepository(private val studentDao: StudentDao) {
    
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()
    
    fun searchStudents(query: String): Flow<List<StudentEntity>> = studentDao.searchStudents(query)
    
    fun getStudentsByGrade(grade: String): Flow<List<StudentEntity>> = studentDao.getStudentsByGrade(grade)
    
    fun getStudentsByClass(grade: String, section: String): Flow<List<StudentEntity>> = 
        studentDao.getStudentsByClass(grade, section)
    
    fun getAllGrades(): Flow<List<String>> = studentDao.getAllGrades()
    
    fun getSectionsByGrade(grade: String): Flow<List<String>> = studentDao.getSectionsByGrade(grade)
    
    suspend fun getStudentById(id: Long): StudentEntity? = studentDao.getStudentById(id)
    
    suspend fun insert(student: StudentEntity): Long = studentDao.insert(student)
    
    suspend fun insertAll(students: List<StudentEntity>) = studentDao.insertAll(students)
    
    suspend fun update(student: StudentEntity) = studentDao.update(student)
    
    suspend fun delete(student: StudentEntity) = studentDao.delete(student)
    
    suspend fun getStudentCount(): Int = studentDao.getStudentCount()
    
    suspend fun getStudentCountByClass(grade: String, section: String): Int = 
        studentDao.getStudentCountByClass(grade, section)
    
    suspend fun deleteAll() = studentDao.deleteAll()
}
