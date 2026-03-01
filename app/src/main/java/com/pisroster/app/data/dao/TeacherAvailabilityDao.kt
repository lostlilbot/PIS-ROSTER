package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.TeacherAvailabilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherAvailabilityDao {
    @Query("SELECT * FROM teacher_availability")
    fun getAllAvailability(): Flow<List<TeacherAvailabilityEntity>>
    
    @Query("SELECT * FROM teacher_availability WHERE teacherId = :teacherId")
    fun getAvailabilityByTeacher(teacherId: Long): Flow<List<TeacherAvailabilityEntity>>
    
    @Query("SELECT * FROM teacher_availability WHERE teacherId = :teacherId AND dayOfWeek = :day AND isAvailable = 1")
    suspend fun getAvailableSlots(teacherId: Long, day: Int): List<TeacherAvailabilityEntity>
    
    @Query("SELECT * FROM teacher_availability WHERE teacherId = :teacherId AND dayOfWeek = :day AND timeSlotId = :timeSlotId")
    suspend fun getAvailability(teacherId: Long, day: Int, timeSlotId: Long): TeacherAvailabilityEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(availability: TeacherAvailabilityEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(availabilities: List<TeacherAvailabilityEntity>)
    
    @Update
    suspend fun update(availability: TeacherAvailabilityEntity)
    
    @Delete
    suspend fun delete(availability: TeacherAvailabilityEntity)
    
    @Query("DELETE FROM teacher_availability WHERE teacherId = :teacherId")
    suspend fun deleteByTeacher(teacherId: Long)
    
    @Query("DELETE FROM teacher_availability")
    suspend fun deleteAll()
}
