package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Time slot for the school day
 * Default: 7:30-8:15, 8:15-9:00, 9:00-9:45, 9:45-10:05 (break), 10:05-10:50, etc.
 */
@Entity(tableName = "time_slots")
data class TimeSlotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val periodNumber: Int, // 1, 2, 3, 4, 5, 6, 7, 8
    val startTime: String, // "07:30"
    val endTime: String, // "08:15"
    val isBreak: Boolean = false,
    val breakDuration: Int = 0, // minutes if isBreak
    val createdAt: Long = System.currentTimeMillis()
)
