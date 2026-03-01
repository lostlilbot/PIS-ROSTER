package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Timetable entry - assigns a subject to a class at a specific time/day
 */
@Entity(tableName = "timetable_entries")
data class TimetableEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val grade: String, // K, 1, 2, ..., 11
    val section: String = "A", // A, B, C
    val dayOfWeek: Int, // 1 = Monday, 5 = Friday
    val timeSlotId: Long, // Reference to time slot
    val subjectId: Long, // Reference to subject
    val teacherId: Long, // Reference to teacher
    val isHomeroom: Boolean = false, // Homeroom period
    val createdAt: Long = System.currentTimeMillis()
)
