package com.pisroster.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pisroster.app.data.dao.*
import com.pisroster.app.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        TeacherEntity::class,
        StudentEntity::class,
        SubjectEntity::class,
        TimeSlotEntity::class,
        TimetableEntryEntity::class,
        AttendanceRecordEntity::class,
        DocumentEntity::class,
        TeacherAvailabilityEntity::class,
        SchoolSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PISDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun teacherDao(): TeacherDao
    abstract fun studentDao(): StudentDao
    abstract fun subjectDao(): SubjectDao
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun timetableEntryDao(): TimetableEntryDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun documentDao(): DocumentDao
    abstract fun teacherAvailabilityDao(): TeacherAvailabilityDao
    abstract fun schoolSettingsDao(): SchoolSettingsDao
    
    companion object {
        @Volatile
        private var INSTANCE: PISDatabase? = null
        
        fun getDatabase(context: Context): PISDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PISDatabase::class.java,
                    "pis_roster_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDefaultData(database)
                    }
                }
            }
        }
        
        private suspend fun populateDefaultData(database: PISDatabase) {
            // Insert default school settings
            database.schoolSettingsDao().insert(SchoolSettingsEntity())
            
            // Insert default time slots
            val defaultTimeSlots = listOf(
                TimeSlotEntity(periodNumber = 1, startTime = "07:30", endTime = "08:15"),
                TimeSlotEntity(periodNumber = 2, startTime = "08:15", endTime = "09:00"),
                TimeSlotEntity(periodNumber = 3, startTime = "09:00", endTime = "09:45"),
                TimeSlotEntity(periodNumber = 4, startTime = "09:45", endTime = "10:05", isBreak = true, breakDuration = 20),
                TimeSlotEntity(periodNumber = 5, startTime = "10:05", endTime = "10:50"),
                TimeSlotEntity(periodNumber = 6, startTime = "10:50", endTime = "11:35"),
                TimeSlotEntity(periodNumber = 7, startTime = "11:35", endTime = "12:20"),
                TimeSlotEntity(periodNumber = 8, startTime = "12:20", endTime = "13:05"),
                TimeSlotEntity(periodNumber = 9, startTime = "13:05", endTime = "13:50", isBreak = true, breakDuration = 10),
                TimeSlotEntity(periodNumber = 10, startTime = "13:50", endTime = "14:35"),
                TimeSlotEntity(periodNumber = 11, startTime = "14:35", endTime = "15:20"),
                TimeSlotEntity(periodNumber = 12, startTime = "15:20", endTime = "16:05")
            )
            database.timeSlotDao().insertAll(defaultTimeSlots)
            
            // Insert default subjects
            val defaultSubjects = listOf(
                SubjectEntity(name = "English", code = "ENG", hoursPerWeek = 5, gradeLevel = "ALL"),
                SubjectEntity(name = "Mathematics", code = "MATH", hoursPerWeek = 5, gradeLevel = "ALL"),
                SubjectEntity(name = "Science", code = "SCI", hoursPerWeek = 4, gradeLevel = "ALL"),
                SubjectEntity(name = "Social Studies", code = "SOC", hoursPerWeek = 3, gradeLevel = "ALL"),
                SubjectEntity(name = "Physical Education", code = "PE", hoursPerWeek = 2, gradeLevel = "ALL"),
                SubjectEntity(name = "Art", code = "ART", hoursPerWeek = 2, gradeLevel = "ALL"),
                SubjectEntity(name = "Music", code = "MUS", hoursPerWeek = 1, gradeLevel = "ALL"),
                SubjectEntity(name = "Spanish", code = "SPA", hoursPerWeek = 4, gradeLevel = "ALL"),
                SubjectEntity(name = "Computer Science", code = "CS", hoursPerWeek = 2, gradeLevel = "ALL"),
                SubjectEntity(name = "Homeroom", code = "HR", hoursPerWeek = 5, gradeLevel = "ALL")
            )
            database.subjectDao().insertAll(defaultSubjects)
        }
    }
}
