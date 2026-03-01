package com.pisroster.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pisroster.app.data.entity.StudentEntity
import com.pisroster.app.data.entity.TeacherEntity
import com.pisroster.app.data.entity.UserEntity
import com.pisroster.app.data.repository.StudentRepository
import com.pisroster.app.data.repository.TeacherRepository
import com.pisroster.app.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch

data class DashboardState(
    val isLoading: Boolean = false,
    val teacherCount: Int = 0,
    val studentCount: Int = 0,
    val teachers: List<TeacherEntity> = emptyList(),
    val students: List<StudentEntity> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null
)

class DashboardViewModel(
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()
    
    init {
        loadDashboardData()
    }
    
    fun loadDashboardData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val teacherCount = teacherRepository.getTeacherCount()
                val studentCount = studentRepository.getStudentCount()
                
                // Use combine to properly handle both flows
                kotlinx.coroutines.flow.combine(
                    teacherRepository.getAllTeachers(),
                    studentRepository.getAllStudents()
                ) { teachers, students ->
                    Pair(teachers, students)
                }.collect { (teachers, students) ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            teacherCount = teacherCount,
                            studentCount = studentCount,
                            teachers = teachers,
                            students = students
                        ) 
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to load data: ${e.message}"
                    ) 
                }
            }
        }
    }
    
    fun addTeacher(name: String, email: String?, phone: String?, grades: String, subjects: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val teacher = TeacherEntity(
                    name = name,
                    email = email,
                    phone = phone,
                    gradesTaught = grades,
                    subjectsTaught = subjects
                )
                teacherRepository.insert(teacher)
                
                // Also create user account for teacher
                val user = UserEntity(
                    username = name.lowercase().replace(" ", "."),
                    password = "teacher123", // Default password
                    role = "TEACHER",
                    fullName = name
                )
                userRepository.insert(user)
                
                _state.update { 
                    it.copy(
                        isLoading = false,
                        successMessage = "Teacher added successfully"
                    ) 
                }
                loadDashboardData()
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to add teacher: ${e.message}"
                    ) 
                }
            }
        }
    }
    
    fun addStudent(name: String, grade: String, section: String, parentName: String?, parentPhone: String?, parentEmail: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val student = StudentEntity(
                    name = name,
                    grade = grade,
                    section = section,
                    parentName = parentName,
                    parentPhone = parentPhone,
                    parentEmail = parentEmail
                )
                studentRepository.insert(student)
                
                // Also create user account for student
                val user = UserEntity(
                    username = name.lowercase().replace(" ", "."),
                    password = "student123", // Default password
                    role = "STUDENT",
                    fullName = name
                )
                userRepository.insert(user)
                
                _state.update { 
                    it.copy(
                        isLoading = false,
                        successMessage = "Student added successfully"
                    ) 
                }
                loadDashboardData()
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to add student: ${e.message}"
                    ) 
                }
            }
        }
    }
    
    fun deleteTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            try {
                teacherRepository.delete(teacher)
                _state.update { it.copy(successMessage = "Teacher deleted") }
                loadDashboardData()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to delete teacher") }
            }
        }
    }
    
    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch {
            try {
                studentRepository.delete(student)
                _state.update { it.copy(successMessage = "Student deleted") }
                loadDashboardData()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to delete student") }
            }
        }
    }
    
    fun clearMessages() {
        _state.update { it.copy(error = null, successMessage = null) }
    }
    
    class Factory(
        private val teacherRepository: TeacherRepository,
        private val studentRepository: StudentRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(teacherRepository, studentRepository, userRepository) as T
        }
    }
}
