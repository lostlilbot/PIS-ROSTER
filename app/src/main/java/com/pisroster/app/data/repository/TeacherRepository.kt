package com.pisroster.app.data.repository

import com.pisroster.app.data.dao.TeacherDao
import com.pisroster.app.data.entity.TeacherEntity
import kotlinx.coroutines.flow.Flow

class TeacherRepository(private val teacherDao: TeacherDao) {
    
    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()
    
    fun searchTeachers(query: String): Flow<List<TeacherEntity>> = teacherDao.searchTeachers(query)
    
    fun getTeachersByGrade(grade: String): Flow<List<TeacherEntity>> = teacherDao.getTeachersByGrade(grade)
    
    fun getTeachersBySubject(subject: String): Flow<List<TeacherEntity>> = teacherDao.getTeachersBySubject(subject)
    
    fun getAllHomeroomTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllHomeroomTeachers()
    
    suspend fun getTeacherById(id: Long): TeacherEntity? = teacherDao.getTeacherById(id)
    
    suspend fun getHomeroomTeacher(grade: String, section: String): TeacherEntity? = 
        teacherDao.getHomeroomTeacher(grade, section)
    
    suspend fun insert(teacher: TeacherEntity): Long = teacherDao.insert(teacher)
    
    suspend fun insertAll(teachers: List<TeacherEntity>) = teacherDao.insertAll(teachers)
    
    suspend fun update(teacher: TeacherEntity) = teacherDao.update(teacher)
    
    suspend fun delete(teacher: TeacherEntity) = teacherDao.delete(teacher)
    
    suspend fun getTeacherCount(): Int = teacherDao.getTeacherCount()
    
    suspend fun deleteAll() = teacherDao.deleteAll()
}
