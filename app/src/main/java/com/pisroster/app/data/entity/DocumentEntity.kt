package com.pisroster.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Document entity for storing files (lesson plans, homework, announcements)
 */
@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val folderName: String, // Lesson Plans, Homework, Announcements, Submissions
    val fileName: String, // Original file name
    val filePath: String, // Internal storage path
    val fileType: String, // PDF, IMAGE, WORD, etc.
    val fileSize: Long,
    val uploadedBy: Long, // Teacher/Admin user ID
    val grade: String? = null, // Applicable grade (optional)
    val isPublic: Boolean = true, // Visible to students
    val allowComments: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
