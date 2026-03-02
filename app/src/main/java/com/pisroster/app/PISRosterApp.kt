package com.pisroster.app

import android.app.Application
import android.util.Log
import com.pisroster.app.data.PISDatabase
import com.pisroster.app.data.TestDataInitializer
import com.pisroster.app.data.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PISRosterApp : Application() {
    
    // Flag to track if app is fully initialized
    var isReady = false
        private set
    
    val database: PISDatabase by lazy {
        try {
            Log.i("PISRosterApp", "Initializing database...")
            val db = PISDatabase.getDatabase(this)
            Log.i("PISRosterApp", "Database initialized successfully")
            db
        } catch (e: Exception) {
            Log.e("PISRosterApp", "Failed to initialize database", e)
            // Return a minimal database or rethrow
            throw e
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
        
        // Initialize test data - run synchronously to ensure it's ready before UI starts
        // Database is already initialized via lazy property, so it's ready
        try {
            Log.i("PISRosterApp", "Checking for existing data...")
            runBlocking(Dispatchers.IO) {
                try {
                    // Check if admin exists (data already initialized)
                    val adminCount = userRepository.getCountByRole("ADMIN")
                    
                    if (adminCount == 0) {
                        Log.i("PISRosterApp", "No admin found, initializing test data...")
                        val testDataInitializer = TestDataInitializer(
                            userRepository,
                            teacherRepository,
                            studentRepository
                        )
                        testDataInitializer.initializeTestData()
                        Log.i("PISRosterApp", "Test data initialized successfully")
                    } else {
                        Log.i("PISRosterApp", "Data already exists ($adminCount admins found)")
                    }
                    
                    isReady = true
                    Log.i("PISRosterApp", "Application ready")
                } catch (e: Exception) {
                    Log.e("PISRosterApp", "Error during data initialization", e)
                    // Mark as ready anyway to allow app to try starting
                    isReady = true
                }
            }
        } catch (e: Exception) {
            Log.e("PISRosterApp", "Error during initialization", e)
            // Continue anyway - the app might work with partial data
            isReady = true
        }
    }
    
    companion object {
        lateinit var instance: PISRosterApp
            private set
    }
}
