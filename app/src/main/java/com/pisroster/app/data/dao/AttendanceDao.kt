package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.AttendanceRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<AttendanceRecordEntity>>
    
    @Query("SELECT * FROM attendance_records WHERE id = :id")
    suspend fun getRecordById(id: Long): AttendanceRecordEntity?
    
    @Query("SELECT * FROM attendance_records WHERE date = :date")
    fun getRecordsByDate(date: Long): Flow<List<AttendanceRecordEntity>>
    
    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId ORDER BY date DESC")
    fun getRecordsByStudent(studentId: Long): Flow<List<AttendanceRecordEntity>>
    
    @Query("SELECT * FROM attendance_records WHERE grade = :grade AND section = :section AND date = :date")
    fun getRecordsByClassAndDate(grade: String, section: String, date: Long): Flow<List<AttendanceRecordEntity>>
    
    @Query("SELECT * FROM attendance_records WHERE grade = :grade AND section = :section AND date BETWEEN :startDate AND :endDate ORDER BY date")
    fun getRecordsByClassAndDateRange(grade: String, section: String, startDate: Long, endDate: Long): Flow<List<AttendanceRecordEntity>>
    
    @Query("SELECT COUNT(*) FROM attendance_records WHERE studentId = :studentId AND status = :status")
    suspend fun getCountByStudentAndStatus(studentId: Long, status: String): Int
    
    @Query("SELECT COUNT(*) FROM attendance_records WHERE grade = :grade AND section = :section AND date = :date AND status = :status")
    suspend fun getCountByClassDateAndStatus(grade: String, section: String, date: Long, status: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: AttendanceRecordEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<AttendanceRecordEntity>)
    
    @Update
    suspend fun update(record: AttendanceRecordEntity)
    
    @Delete
    suspend fun delete(record: AttendanceRecordEntity)
    
    @Query("DELETE FROM attendance_records WHERE date = :date AND grade = :grade AND section = :section")
    suspend fun deleteByClassAndDate(grade: String, section: String, date: Long)
    
    @Query("DELETE FROM attendance_records")
    suspend fun deleteAll()
}
