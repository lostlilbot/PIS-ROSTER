package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY grade, section, name ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>
    
    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Long): StudentEntity?
    
    @Query("SELECT * FROM students WHERE name LIKE '%' || :query || '%'")
    fun searchStudents(query: String): Flow<List<StudentEntity>>
    
    @Query("SELECT * FROM students WHERE grade = :grade ORDER BY section, name ASC")
    fun getStudentsByGrade(grade: String): Flow<List<StudentEntity>>
    
    @Query("SELECT * FROM students WHERE grade = :grade AND section = :section ORDER BY name ASC")
    fun getStudentsByClass(grade: String, section: String): Flow<List<StudentEntity>>
    
    @Query("SELECT DISTINCT grade FROM students ORDER BY CASE grade WHEN 'K' THEN 0 WHEN '1' THEN 1 WHEN '2' THEN 2 WHEN '3' THEN 3 WHEN '4' THEN 4 WHEN '5' THEN 5 WHEN '6' THEN 6 WHEN '7' THEN 7 WHEN '8' THEN 8 WHEN '9' THEN 9 WHEN '10' THEN 10 WHEN '11' THEN 11 ELSE 99 END")
    fun getAllGrades(): Flow<List<String>>
    
    @Query("SELECT DISTINCT section FROM students WHERE grade = :grade ORDER BY section ASC")
    fun getSectionsByGrade(grade: String): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<StudentEntity>)
    
    @Update
    suspend fun update(student: StudentEntity)
    
    @Delete
    suspend fun delete(student: StudentEntity)
    
    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT COUNT(*) FROM students")
    suspend fun getStudentCount(): Int
    
    @Query("SELECT COUNT(*) FROM students WHERE grade = :grade AND section = :section")
    suspend fun getStudentCountByClass(grade: String, section: String): Int
    
    @Query("DELETE FROM students")
    suspend fun deleteAll()
}
