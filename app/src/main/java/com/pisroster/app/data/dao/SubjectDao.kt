package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects ORDER BY name ASC")
    fun getAllSubjects(): Flow<List<SubjectEntity>>
    
    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun getSubjectById(id: Long): SubjectEntity?
    
    @Query("SELECT * FROM subjects WHERE code = :code")
    suspend fun getSubjectByCode(code: String): SubjectEntity?
    
    @Query("SELECT * FROM subjects WHERE gradeLevel LIKE '%' || :grade || '%'")
    fun getSubjectsByGrade(grade: String): Flow<List<SubjectEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subject: SubjectEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subjects: List<SubjectEntity>)
    
    @Update
    suspend fun update(subject: SubjectEntity)
    
    @Delete
    suspend fun delete(subject: SubjectEntity)
    
    @Query("DELETE FROM subjects")
    suspend fun deleteAll()
}
