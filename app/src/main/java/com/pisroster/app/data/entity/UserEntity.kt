package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User entity representing Admin, Teacher, or Student
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String, // Stored as hashed
    val role: String, // ADMIN, TEACHER, STUDENT
    val fullName: String,
    val email: String? = null,
    val phone: String? = null,
    val photoPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
