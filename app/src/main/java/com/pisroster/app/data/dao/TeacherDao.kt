package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.TeacherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teachers ORDER BY name ASC")
    fun getAllTeachers(): Flow<List<TeacherEntity>>
    
    @Query("SELECT * FROM teachers WHERE id = :id")
    suspend fun getTeacherById(id: Long): TeacherEntity?
    
    @Query("SELECT * FROM teachers WHERE name LIKE '%' || :query || '%'")
    fun searchTeachers(query: String): Flow<List<TeacherEntity>>
    
    @Query("SELECT * FROM teachers WHERE gradesTaught LIKE '%' || :grade || '%'")
    fun getTeachersByGrade(grade: String): Flow<List<TeacherEntity>>
    
    @Query("SELECT * FROM teachers WHERE subjectsTaught LIKE '%' || :subject || '%'")
    fun getTeachersBySubject(subject: String): Flow<List<TeacherEntity>>
    
    @Query("SELECT * FROM teachers WHERE isHomeroomTeacher = 1 AND homeroomGrade = :grade AND homeroomSection = :section")
    suspend fun getHomeroomTeacher(grade: String, section: String): TeacherEntity?
    
    @Query("SELECT * FROM teachers WHERE isHomeroomTeacher = 1")
    fun getAllHomeroomTeachers(): Flow<List<TeacherEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teacher: TeacherEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teachers: List<TeacherEntity>)
    
    @Update
    suspend fun update(teacher: TeacherEntity)
    
    @Delete
    suspend fun delete(teacher: TeacherEntity)
    
    @Query("DELETE FROM teachers WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT COUNT(*) FROM teachers")
    suspend fun getTeacherCount(): Int
    
    @Query("DELETE FROM teachers")
    suspend fun deleteAll()
}
