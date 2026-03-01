package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.SchoolSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolSettingsDao {
    @Query("SELECT * FROM school_settings WHERE id = 1")
    fun getSettings(): Flow<SchoolSettingsEntity?>
    
    @Query("SELECT * FROM school_settings WHERE id = 1")
    suspend fun getSettingsSync(): SchoolSettingsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: SchoolSettingsEntity)
    
    @Update
    suspend fun update(settings: SchoolSettingsEntity)
    
    @Query("UPDATE school_settings SET logoPath = :logoPath, updatedAt = :updatedAt WHERE id = 1")
    suspend fun updateLogo(logoPath: String, updatedAt: Long = System.currentTimeMillis())
    
    @Query("UPDATE school_settings SET darkMode = :darkMode, updatedAt = :updatedAt WHERE id = 1")
    suspend fun updateDarkMode(darkMode: Boolean, updatedAt: Long = System.currentTimeMillis())
    
    @Query("UPDATE school_settings SET language = :language, updatedAt = :updatedAt WHERE id = 1")
    suspend fun updateLanguage(language: String, updatedAt: Long = System.currentTimeMillis())
}
