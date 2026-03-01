package com.pisroster.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pisroster.app.data.entity.SchoolSettingsEntity
import com.pisroster.app.data.entity.UserEntity
import com.pisroster.app.data.repository.SettingsRepository
import com.pisroster.app.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUser: UserEntity? = null,
    val isFirstLaunch: Boolean = true,
    val error: String? = null,
    val settings: SchoolSettingsEntity? = null,
    val mustChangePassword: Boolean = false
)

class AuthViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()
    
    init {
        checkFirstLaunch()
    }
    
    private fun checkFirstLaunch() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val settings = settingsRepository.getSettingsSync()
            val hasAdmin = userRepository.getCountByRole("ADMIN") > 0
            
            _state.update { 
                it.copy(
                    isFirstLaunch = !hasAdmin,
                    settings = settings,
                    isLoading = false
                )
            }
        }
    }
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                val user = userRepository.login(username, password)
                if (user != null) {
                    _state.update { 
                        it.copy(
                            isLoggedIn = true,
                            currentUser = user,
                            isLoading = false,
                            mustChangePassword = user.mustChangePassword
                        ) 
                    }
                } else {
                    _state.update { 
                        it.copy(
                            error = "Invalid username or password",
                            isLoading = false
                        ) 
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        error = "Login failed: ${e.message}",
                        isLoading = false
                    ) 
                }
            }
        }
    }
    
    fun createAdminAccount(username: String, password: String, schoolName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                // Create admin user
                val adminUser = UserEntity(
                    username = username,
                    password = password, // In production, hash this!
                    role = "ADMIN",
                    fullName = "Administrator"
                )
                userRepository.insert(adminUser)
                
                // Update school name
                settingsRepository.updateSettings(
                    SchoolSettingsEntity(
                        schoolName = schoolName,
                        language = "en",
                        darkMode = false
                    )
                )
                
                // Login as admin
                val user = userRepository.login(username, password)
                _state.update { 
                    it.copy(
                        isFirstLaunch = false,
                        isLoggedIn = true,
                        currentUser = user,
                        isLoading = false
                    ) 
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        error = "Failed to create account: ${e.message}",
                        isLoading = false
                    ) 
                }
            }
        }
    }
    
    fun logout() {
        _state.update { 
            AuthState(isFirstLaunch = false, settings = it.settings) 
        }
    }
    
    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val user = _state.value.currentUser
            if (user == null) {
                _state.update { it.copy(error = "User not found", isLoading = false) }
                return@launch
            }
            
            // Validate inputs
            if (newPassword.length < 6) {
                _state.update { it.copy(error = "Password must be at least 6 characters", isLoading = false) }
                return@launch
            }
            
            if (newPassword != confirmPassword) {
                _state.update { it.copy(error = "Passwords do not match", isLoading = false) }
                return@launch
            }
            
            // Verify current password
            val verifiedUser = userRepository.login(user.username, currentPassword)
            if (verifiedUser == null) {
                _state.update { it.copy(error = "Current password is incorrect", isLoading = false) }
                return@launch
            }
            
            try {
                // Update password
                userRepository.updatePassword(user.id, newPassword)
                
                // Get updated user
                val updatedUser = userRepository.getUserById(user.id)
                
                _state.update { 
                    it.copy(
                        currentUser = updatedUser,
                        mustChangePassword = false,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        error = "Failed to change password: ${e.message}",
                        isLoading = false
                    ) 
                }
            }
        }
    }
    
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
    
    class Factory(
        private val userRepository: UserRepository,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(userRepository, settingsRepository) as T
        }
    }
}
