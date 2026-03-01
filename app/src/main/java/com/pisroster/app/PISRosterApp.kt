package com.pisroster.app

import android.app.Application
import android.util.Log
import com.pisroster.app.data.PISDatabase
import com.pisroster.app.data.TestDataInitializer
import com.pisroster.app.data.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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
    
    // Application scope for async operations
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize test data with error handling
        applicationScope.launch {
            try {
                val testDataInitializer = TestDataInitializer(
                    userRepository,
                    teacherRepository,
                    studentRepository
                )
                testDataInitializer.initializeTestData()
            } catch (e: Exception) {
                Log.e("PISRosterApp", "Error initializing test data", e)
            }
        }
    }
    
    companion object {
        lateinit var instance: PISRosterApp
            private set
    }
}
