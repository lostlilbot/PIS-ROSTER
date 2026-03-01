package com.pisroster.app.data.repository

import com.pisroster.app.data.dao.*
import com.pisroster.app.data.entity.*
import kotlinx.coroutines.flow.Flow

class TimetableRepository(
    private val timetableEntryDao: TimetableEntryDao,
    private val timeSlotDao: TimeSlotDao,
    private val subjectDao: SubjectDao,
    private val teacherAvailabilityDao: TeacherAvailabilityDao
) {
    // Timetable Entries
    fun getAllEntries(): Flow<List<TimetableEntryEntity>> = timetableEntryDao.getAllEntries()
    
    fun getEntriesByClass(grade: String, section: String): Flow<List<TimetableEntryEntity>> = 
        timetableEntryDao.getEntriesByClass(grade, section)
    
    fun getEntriesByTeacher(teacherId: Long): Flow<List<TimetableEntryEntity>> = 
        timetableEntryDao.getEntriesByTeacher(teacherId)
    
    fun getEntriesByClassAndDay(grade: String, section: String, day: Int): Flow<List<TimetableEntryEntity>> =
        timetableEntryDao.getEntriesByClassAndDay(grade, section, day)
    
    fun getEntriesByTeacherAndDay(teacherId: Long, day: Int): Flow<List<TimetableEntryEntity>> =
        timetableEntryDao.getEntriesByTeacherAndDay(teacherId, day)
    
    fun getAllGrades(): Flow<List<String>> = timetableEntryDao.getAllGrades()
    
    fun getSectionsByGrade(grade: String): Flow<List<String>> = timetableEntryDao.getSectionsByGrade(grade)
    
    suspend fun getEntryById(id: Long): TimetableEntryEntity? = timetableEntryDao.getEntryById(id)
    
    suspend fun insertEntry(entry: TimetableEntryEntity): Long = timetableEntryDao.insert(entry)
    
    suspend fun insertAllEntries(entries: List<TimetableEntryEntity>) = timetableEntryDao.insertAll(entries)
    
    suspend fun updateEntry(entry: TimetableEntryEntity) = timetableEntryDao.update(entry)
    
    suspend fun deleteEntry(entry: TimetableEntryEntity) = timetableEntryDao.delete(entry)
    
    suspend fun deleteByClass(grade: String, section: String) = timetableEntryDao.deleteByClass(grade, section)
    
    suspend fun deleteByTeacher(teacherId: Long) = timetableEntryDao.deleteByTeacher(teacherId)
    
    suspend fun deleteAllEntries() = timetableEntryDao.deleteAll()
    
    // Time Slots
    fun getAllTimeSlots(): Flow<List<TimeSlotEntity>> = timeSlotDao.getAllTimeSlots()
    
    fun getClassTimeSlots(): Flow<List<TimeSlotEntity>> = timeSlotDao.getClassTimeSlots()
    
    suspend fun getTimeSlotById(id: Long): TimeSlotEntity? = timeSlotDao.getTimeSlotById(id)
    
    suspend fun insertTimeSlot(timeSlot: TimeSlotEntity): Long = timeSlotDao.insert(timeSlot)
    
    suspend fun insertAllTimeSlots(timeSlots: List<TimeSlotEntity>) = timeSlotDao.insertAll(timeSlots)
    
    suspend fun updateTimeSlot(timeSlot: TimeSlotEntity) = timeSlotDao.update(timeSlot)
    
    suspend fun deleteTimeSlot(timeSlot: TimeSlotEntity) = timeSlotDao.delete(timeSlot)
    
    suspend fun deleteAllTimeSlots() = timeSlotDao.deleteAll()
    
    // Subjects
    fun getAllSubjects(): Flow<List<SubjectEntity>> = subjectDao.getAllSubjects()
    
    fun getSubjectsByGrade(grade: String): Flow<List<SubjectEntity>> = subjectDao.getSubjectsByGrade(grade)
    
    suspend fun getSubjectById(id: Long): SubjectEntity? = subjectDao.getSubjectById(id)
    
    suspend fun getSubjectByCode(code: String): SubjectEntity? = subjectDao.getSubjectByCode(code)
    
    suspend fun insertSubject(subject: SubjectEntity): Long = subjectDao.insert(subject)
    
    suspend fun insertAllSubjects(subjects: List<SubjectEntity>) = subjectDao.insertAll(subjects)
    
    suspend fun updateSubject(subject: SubjectEntity) = subjectDao.update(subject)
    
    suspend fun deleteSubject(subject: SubjectEntity) = subjectDao.delete(subject)
    
    suspend fun deleteAllSubjects() = subjectDao.deleteAll()
    
    // Teacher Availability
    fun getAllAvailability(): Flow<List<TeacherAvailabilityEntity>> = teacherAvailabilityDao.getAllAvailability()
    
    fun getAvailabilityByTeacher(teacherId: Long): Flow<List<TeacherAvailabilityEntity>> = 
        teacherAvailabilityDao.getAvailabilityByTeacher(teacherId)
    
    suspend fun getAvailableSlots(teacherId: Long, day: Int): List<TeacherAvailabilityEntity> = 
        teacherAvailabilityDao.getAvailableSlots(teacherId, day)
    
    suspend fun getAvailability(teacherId: Long, day: Int, timeSlotId: Long): TeacherAvailabilityEntity? = 
        teacherAvailabilityDao.getAvailability(teacherId, day, timeSlotId)
    
    suspend fun insertAvailability(availability: TeacherAvailabilityEntity): Long = 
        teacherAvailabilityDao.insert(availability)
    
    suspend fun insertAllAvailability(availabilities: List<TeacherAvailabilityEntity>) = 
        teacherAvailabilityDao.insertAll(availabilities)
    
    suspend fun deleteAvailability(availability: TeacherAvailabilityEntity) = 
        teacherAvailabilityDao.delete(availability)
    
    suspend fun deleteByTeacher(teacherId: Long) = teacherAvailabilityDao.deleteByTeacher(teacherId)
    
    suspend fun deleteAllAvailability() = teacherAvailabilityDao.deleteAll()
}
