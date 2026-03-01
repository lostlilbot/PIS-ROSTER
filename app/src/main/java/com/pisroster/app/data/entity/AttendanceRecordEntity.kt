package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Attendance record for a student on a specific date
 */
@Entity(tableName = "attendance_records")
data class AttendanceRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val grade: String,
    val section: String,
    val date: Long, // Date in millis (start of day)
    val timetableEntryId: Long? = null, // Which class period
    val status: String, // PRESENT, ABSENT, LATE
    val remarks: String? = null,
    val markedBy: Long, // Teacher ID who marked
    val markedAt: Long = System.currentTimeMillis()
)
