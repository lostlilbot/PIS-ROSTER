package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Subject entity - e.g., ENG, MATH, SCI, P.E.
 */
@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // e.g., "English", "Mathematics"
    val code: String, // e.g., "ENG", "MATH"
    val hoursPerWeek: Int = 0, // Required hours per week
    val gradeLevel: String = "", // e.g., "8-11" or "K" or "ALL"
    val createdAt: Long = System.currentTimeMillis()
)
