package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Student entity with parent/guardian information
 */
@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long? = null, // Link to user account
    val name: String,
    val grade: String, // K, 1, 2, ..., 11
    val section: String = "A", // A, B, C, etc.
    val photoPath: String? = null,
    val parentName: String? = null,
    val parentPhone: String? = null,
    val parentEmail: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
