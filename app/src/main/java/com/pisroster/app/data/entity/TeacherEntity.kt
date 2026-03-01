package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Teacher entity with teaching assignments
 */
@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long? = null, // Link to user account
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val photoPath: String? = null,
    val gradesTaught: String = "", // Comma-separated grades: "K,1,2,3"
    val subjectsTaught: String = "", // Comma-separated subjects: "ENG,MATH,SCI"
    val isHomeroomTeacher: Boolean = false,
    val homeroomGrade: String? = null, // Grade if homeroom teacher
    val homeroomSection: String? = null, // Section if homeroom teacher
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
