package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>
    
    @Query("SELECT * FROM documents WHERE id = :id")
    suspend fun getDocumentById(id: Long): DocumentEntity?
    
    @Query("SELECT * FROM documents WHERE folderName = :folder ORDER BY createdAt DESC")
    fun getDocumentsByFolder(folder: String): Flow<List<DocumentEntity>>
    
    @Query("SELECT * FROM documents WHERE folderName = :folder AND grade = :grade ORDER BY createdAt DESC")
    fun getDocumentsByFolderAndGrade(folder: String, grade: String): Flow<List<DocumentEntity>>
    
    @Query("SELECT * FROM documents WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchDocuments(query: String): Flow<List<DocumentEntity>>
    
    @Query("SELECT DISTINCT folderName FROM documents")
    fun getAllFolders(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: DocumentEntity): Long
    
    @Update
    suspend fun update(document: DocumentEntity)
    
    @Delete
    suspend fun delete(document: DocumentEntity)
    
    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM documents")
    suspend fun deleteAll()
}
