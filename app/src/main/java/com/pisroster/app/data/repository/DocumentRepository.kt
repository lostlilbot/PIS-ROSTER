package com.pisroster.app.data.repository

import com.pisroster.app.data.dao.DocumentDao
import com.pisroster.app.data.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

class DocumentRepository(private val documentDao: DocumentDao) {
    
    fun getAllDocuments(): Flow<List<DocumentEntity>> = documentDao.getAllDocuments()
    
    fun getDocumentsByFolder(folder: String): Flow<List<DocumentEntity>> = 
        documentDao.getDocumentsByFolder(folder)
    
    fun getDocumentsByFolderAndGrade(folder: String, grade: String): Flow<List<DocumentEntity>> = 
        documentDao.getDocumentsByFolderAndGrade(folder, grade)
    
    fun searchDocuments(query: String): Flow<List<DocumentEntity>> = 
        documentDao.searchDocuments(query)
    
    fun getAllFolders(): Flow<List<String>> = documentDao.getAllFolders()
    
    suspend fun getDocumentById(id: Long): DocumentEntity? = documentDao.getDocumentById(id)
    
    suspend fun insert(document: DocumentEntity): Long = documentDao.insert(document)
    
    suspend fun update(document: DocumentEntity) = documentDao.update(document)
    
    suspend fun delete(document: DocumentEntity) = documentDao.delete(document)
    
    suspend fun deleteById(id: Long) = documentDao.deleteById(id)
    
    suspend fun deleteAll() = documentDao.deleteAll()
}
