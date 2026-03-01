package com.pisroster.app.data

import com.pisroster.app.data.entity.StudentEntity
import com.pisroster.app.data.entity.TeacherEntity
import com.pisroster.app.data.entity.UserEntity
import com.pisroster.app.data.repository.StudentRepository
import com.pisroster.app.data.repository.TeacherRepository
import com.pisroster.app.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Test data initializer for populating the database with test users
 */
class TestDataInitializer(
    private val userRepository: UserRepository,
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository
) {
    // Default password for all test users - must be changed on first login
    private val defaultPassword = "password123"
    
    // Grade levels for students
    private val grades = listOf("K", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
    
    // Subjects that teachers can teach
    private val subjects = listOf(
        "MATH", "ENG", "SCI", "HIST", "GEO", "ART", "MUSIC", "PE",
        "SPA", "PHYS", "CHEM", "BIO", "COMP", "ECON", "LANG"
    )
    
    // First names for generating test data
    private val firstNames = listOf(
        "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda",
        "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica",
        "Thomas", "Sarah", "Charles", "Karen", "Christopher", "Lisa", "Daniel", "Nancy",
        "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley"
    )
    
    // Last names for generating test data
    private val lastNames = listOf(
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
        "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker"
    )
    
    /**
     * Initialize all test data
     */
    suspend fun initializeTestData() = withContext(Dispatchers.IO) {
        // Check if data already exists
        val adminCount = userRepository.getCountByRole("ADMIN")
        if (adminCount > 0) {
            return@withContext // Data already initialized
        }
        
        // Create Admin user
        createAdmin()
        
        // Create 15 Teachers
        createTeachers(15)
        
        // Create 10 students for each grade (K-11 = 12 grades = 120 students)
        grades.forEach { grade ->
            createStudentsForGrade(grade, 10)
        }
    }
    
    private suspend fun createAdmin() {
        val admin = UserEntity(
            username = "admin",
            password = defaultPassword,
            role = "ADMIN",
            fullName = "System Administrator",
            email = "admin@school.edu",
            phone = "+1-555-0100",
            mustChangePassword = true
        )
        userRepository.insert(admin)
    }
    
    private suspend fun createTeachers(count: Int) {
        val usedNames = mutableSetOf<String>()
        
        repeat(count) { index ->
            val firstName = getUniqueName(firstNames, usedNames)
            val lastName = getUniqueName(lastNames, usedNames)
            val fullName = "$firstName $lastName"
            
            // Create user account for teacher
            val username = "${firstName.lowercase()}.${lastName.lowercase()}"
            val teacherUser = UserEntity(
                username = username,
                password = defaultPassword,
                role = "TEACHER",
                fullName = fullName,
                email = "${firstName.lowercase()}.${lastName.lowercase()}@school.edu",
                phone = "+1-555-${String.format("%04d", 1000 + index)}",
                mustChangePassword = true
            )
            val userId = userRepository.insert(teacherUser)
            
            // Assign random grades and subjects
            val numGrades = (1..3).random()
            val selectedGrades = grades.shuffled().take(numGrades).sortedBy { 
                if (it == "K") 0 else it.toInt() 
            }
            val numSubjects = (2..4).random()
            val selectedSubjects = subjects.shuffled().take(numSubjects)
            
            // Determine if this teacher is a homeroom teacher
            val isHomeroom = index < 6 // First 6 teachers become homeroom teachers
            val homeroomGrade = if (isHomeroom) grades.getOrNull(index) else null
            val homeroomSection = if (isHomeroom) "A" else null
            
            val teacher = TeacherEntity(
                userId = userId,
                name = fullName,
                email = "${firstName.lowercase()}.${lastName.lowercase()}@school.edu",
                phone = "+1-555-${String.format("%04d", 1000 + index)}",
                gradesTaught = selectedGrades.joinToString(","),
                subjectsTaught = selectedSubjects.joinToString(","),
                isHomeroomTeacher = isHomeroom,
                homeroomGrade = homeroomGrade,
                homeroomSection = homeroomSection
            )
            teacherRepository.insert(teacher)
        }
    }
    
    private suspend fun createStudentsForGrade(grade: String, count: Int) {
        val usedNames = mutableSetOf<String>()
        
        repeat(count) { index ->
            val firstName = getUniqueName(firstNames, usedNames)
            val lastName = getUniqueName(lastNames, usedNames)
            val fullName = "$firstName $lastName"
            
            // Create user account for student
            val username = "${firstName.lowercase()}.${lastName.lowercase()}$grade"
            val studentUser = UserEntity(
                username = username,
                password = defaultPassword,
                role = "STUDENT",
                fullName = fullName,
                email = "${firstName.lowercase()}.${lastName.lowercase()}@student.school.edu",
                phone = "+1-555-${String.format("%04d", 2000 + index)}",
                mustChangePassword = true
            )
            val userId = userRepository.insert(studentUser)
            
            // Determine section (A, B, C, etc.)
            val section = when {
                index < 4 -> "A"
                index < 7 -> "B"
                else -> "C"
            }
            
            val student = StudentEntity(
                userId = userId,
                name = fullName,
                grade = grade,
                section = section,
                parentName = "$lastName Family",
                parentPhone = "+1-555-${String.format("%04d", 3000 + index)}",
                parentEmail = "parent.${lastName.lowercase()}@email.com"
            )
            studentRepository.insert(student)
        }
    }
    
    private fun getUniqueName(names: List<String>, usedNames: Set<String>): String {
        var name: String
        var attempts = 0
        do {
            name = names.random()
            attempts++
        } while (usedNames.contains(name) && attempts < names.size)
        return name
    }
}
