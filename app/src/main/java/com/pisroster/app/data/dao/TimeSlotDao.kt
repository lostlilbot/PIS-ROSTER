package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.TimeSlotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeSlotDao {
    @Query("SELECT * FROM time_slots ORDER BY periodNumber ASC")
    fun getAllTimeSlots(): Flow<List<TimeSlotEntity>>
    
    @Query("SELECT * FROM time_slots WHERE isBreak = 0 ORDER BY periodNumber ASC")
    fun getClassTimeSlots(): Flow<List<TimeSlotEntity>>
    
    @Query("SELECT * FROM time_slots WHERE id = :id")
    suspend fun getTimeSlotById(id: Long): TimeSlotEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timeSlot: TimeSlotEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(timeSlots: List<TimeSlotEntity>)
    
    @Update
    suspend fun update(timeSlot: TimeSlotEntity)
    
    @Delete
    suspend fun delete(timeSlot: TimeSlotEntity)
    
    @Query("DELETE FROM time_slots")
    suspend fun deleteAll()
}
