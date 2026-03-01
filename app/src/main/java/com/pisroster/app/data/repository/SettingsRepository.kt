package com.pisroster.app.data.repository

import com.pisroster.app.data.dao.SchoolSettingsDao
import com.pisroster.app.data.entity.SchoolSettingsEntity
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SchoolSettingsDao) {
    
    fun getSettings(): Flow<SchoolSettingsEntity?> = settingsDao.getSettings()
    
    suspend fun getSettingsSync(): SchoolSettingsEntity? = settingsDao.getSettingsSync()
    
    suspend fun saveSettings(settings: SchoolSettingsEntity) = settingsDao.insert(settings)
    
    suspend fun updateSettings(settings: SchoolSettingsEntity) = settingsDao.update(settings)
    
    suspend fun updateLogo(logoPath: String) = settingsDao.updateLogo(logoPath)
    
    suspend fun updateDarkMode(darkMode: Boolean) = settingsDao.updateDarkMode(darkMode)
    
    suspend fun updateLanguage(language: String) = settingsDao.updateLanguage(language)
}
