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
    val settings: SchoolSettingsEntity? = null
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
                            isLoading = false
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
