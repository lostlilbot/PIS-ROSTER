package com.pisroster.app.data.repository

import com.pisroster.app.data.dao.AttendanceDao
import com.pisroster.app.data.entity.AttendanceRecordEntity
import kotlinx.coroutines.flow.Flow

class AttendanceRepository(private val attendanceDao: AttendanceDao) {
    
    fun getAllRecords(): Flow<List<AttendanceRecordEntity>> = attendanceDao.getAllRecords()
    
    fun getRecordsByDate(date: Long): Flow<List<AttendanceRecordEntity>> = attendanceDao.getRecordsByDate(date)
    
    fun getRecordsByStudent(studentId: Long): Flow<List<AttendanceRecordEntity>> = 
        attendanceDao.getRecordsByStudent(studentId)
    
    fun getRecordsByClassAndDate(grade: String, section: String, date: Long): Flow<List<AttendanceRecordEntity>> =
        attendanceDao.getRecordsByClassAndDate(grade, section, date)
    
    fun getRecordsByClassAndDateRange(grade: String, section: String, startDate: Long, endDate: Long): 
        Flow<List<AttendanceRecordEntity>> = attendanceDao.getRecordsByClassAndDateRange(grade, section, startDate, endDate)
    
    suspend fun getRecordById(id: Long): AttendanceRecordEntity? = attendanceDao.getRecordById(id)
    
    suspend fun getCountByStudentAndStatus(studentId: Long, status: String): Int = 
        attendanceDao.getCountByStudentAndStatus(studentId, status)
    
    suspend fun getCountByClassDateAndStatus(grade: String, section: String, date: Long, status: String): Int = 
        attendanceDao.getCountByClassDateAndStatus(grade, section, date, status)
    
    suspend fun insert(record: AttendanceRecordEntity): Long = attendanceDao.insert(record)
    
    suspend fun insertAll(records: List<AttendanceRecordEntity>) = attendanceDao.insertAll(records)
    
    suspend fun update(record: AttendanceRecordEntity) = attendanceDao.update(record)
    
    suspend fun delete(record: AttendanceRecordEntity) = attendanceDao.delete(record)
    
    suspend fun deleteByClassAndDate(grade: String, section: String, date: Long) = 
        attendanceDao.deleteByClassAndDate(grade, section, date)
    
    suspend fun deleteAll() = attendanceDao.deleteAll()
}
