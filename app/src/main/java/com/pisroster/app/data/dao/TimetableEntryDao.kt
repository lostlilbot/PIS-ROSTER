package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.TimetableEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableEntryDao {
    @Query("SELECT * FROM timetable_entries")
    fun getAllEntries(): Flow<List<TimetableEntryEntity>>
    
    @Query("SELECT * FROM timetable_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): TimetableEntryEntity?
    
    @Query("SELECT * FROM timetable_entries WHERE grade = :grade AND section = :section ORDER BY dayOfWeek, timeSlotId")
    fun getEntriesByClass(grade: String, section: String): Flow<List<TimetableEntryEntity>>
    
    @Query("SELECT * FROM timetable_entries WHERE teacherId = :teacherId ORDER BY dayOfWeek, timeSlotId")
    fun getEntriesByTeacher(teacherId: Long): Flow<List<TimetableEntryEntity>>
    
    @Query("SELECT * FROM timetable_entries WHERE grade = :grade AND section = :section AND dayOfWeek = :day ORDER BY timeSlotId")
    fun getEntriesByClassAndDay(grade: String, section: String, day: Int): Flow<List<TimetableEntryEntity>>
    
    @Query("SELECT * FROM timetable_entries WHERE teacherId = :teacherId AND dayOfWeek = :day ORDER BY timeSlotId")
    fun getEntriesByTeacherAndDay(teacherId: Long, day: Int): Flow<List<TimetableEntryEntity>>
    
    @Query("SELECT DISTINCT grade FROM timetable_entries ORDER BY grade")
    fun getAllGrades(): Flow<List<String>>
    
    @Query("SELECT DISTINCT section FROM timetable_entries WHERE grade = :grade ORDER BY section")
    fun getSectionsByGrade(grade: String): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: TimetableEntryEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<TimetableEntryEntity>)
    
    @Update
    suspend fun update(entry: TimetableEntryEntity)
    
    @Delete
    suspend fun delete(entry: TimetableEntryEntity)
    
    @Query("DELETE FROM timetable_entries WHERE grade = :grade AND section = :section")
    suspend fun deleteByClass(grade: String, section: String)
    
    @Query("DELETE FROM timetable_entries WHERE teacherId = :teacherId")
    suspend fun deleteByTeacher(teacherId: Long)
    
    @Query("DELETE FROM timetable_entries")
    suspend fun deleteAll()
}
