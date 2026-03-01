package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Teacher availability for timetable generation
 * Stores which days/periods a teacher can teach
 */
@Entity(tableName = "teacher_availability")
data class TeacherAvailabilityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val teacherId: Long,
    val dayOfWeek: Int, // 1-5 (Monday-Friday)
    val timeSlotId: Long,
    val isAvailable: Boolean = true
)
