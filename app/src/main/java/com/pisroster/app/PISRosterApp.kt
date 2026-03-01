package com.pisroster.app

import android.app.Application
import android.util.Log
import com.pisroster.app.data.PISDatabase
import com.pisroster.app.data.TestDataInitializer
import com.pisroster.app.data.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PISRosterApp : Application() {
    
    val database: PISDatabase by lazy {
        try {
            PISDatabase.getDatabase(this)
        } catch (e: Exception) {
            Log.e("PISRosterApp", "Failed to initialize database", e)
            throw e // Let it propagate so we can see the error
        }
    }
    
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
        
        Log.i("PISRosterApp", "Application starting...")
        
        // Initialize test data synchronously to ensure it's ready before UI starts
        try {
            Log.i("PISRosterApp", "Initializing test data...")
            runBlocking(Dispatchers.IO) {
                try {
                    // First ensure the database callback has completed by checking if settings exist
                    val existingSettings = settingsRepository.getSettingsSync()
                    Log.i("PISRosterApp", "Database ready with settings: $existingSettings")
                    
                    // Now initialize test data (this is idempotent - safe to call multiple times)
                    val testDataInitializer = TestDataInitializer(
                        userRepository,
                        teacherRepository,
                        studentRepository
                    )
                    testDataInitializer.initializeTestData()
                    Log.i("PISRosterApp", "Test data initialized successfully")
                } catch (e: Exception) {
                    Log.e("PISRosterApp", "Error during data initialization", e)
                }
            }
        } catch (e: Exception) {
            Log.e("PISRosterApp", "Error during initialization", e)
            // Continue anyway - the app might work with partial data
        }
        
        Log.i("PISRosterApp", "Application ready")
    }
    
    companion object {
        lateinit var instance: PISRosterApp
            private set
    }
}
