package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * School settings - stores school name, logo, and app settings
 */
@Entity(tableName = "school_settings")
data class SchoolSettingsEntity(
    @PrimaryKey
    val id: Int = 1, // Single row
    val schoolName: String = "Progreso International School",
    val logoPath: String? = null,
    val language: String = "en", // en, es
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val attendanceReminderMinutes: Int = 15,
    val classReminderMinutes: Int = 10,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
