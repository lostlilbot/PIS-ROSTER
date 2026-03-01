package com.pisroster.app

import android.app.Application
import com.pisroster.app.data.PISDatabase
import com.pisroster.app.data.repository.*

class PISRosterApp : Application() {
    
    val database: PISDatabase by lazy { PISDatabase.getDatabase(this) }
    
    val userRepository: UserRepository by lazy { UserRepository(database.userDao()) }
    val teacherRepository: TeacherRepository by lazy { TeacherRepository(database.teacherDao()) }
    val studentRepository: StudentRepository by lazy { StudentRepository(database.studentDao()) }
    val timetableRepository: TimetableRepository by lazy { 
        TimetableRepository(
            database.timetableEntryDao(),
            database.timeSlotDao(),
            database.subjectDao(),
            database.teacherAvailabilityDao()
        ) 
    }
    val attendanceRepository: AttendanceRepository by lazy { AttendanceRepository(database.attendanceDao()) }
    val documentRepository: DocumentRepository by lazy { DocumentRepository(database.documentDao()) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(database.schoolSettingsDao()) }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: PISRosterApp
            private set
    }
}
