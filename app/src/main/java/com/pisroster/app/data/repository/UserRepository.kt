package com.pisroster.app.data.repository

import com.pisroster.app.data.dao.UserDao
import com.pisroster.app.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    
    fun getUsersByRole(role: String): Flow<List<UserEntity>> = userDao.getUsersByRole(role)
    
    suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)
    
    suspend fun getUserByUsername(username: String): UserEntity? = userDao.getUserByUsername(username)
    
    suspend fun login(username: String, password: String): UserEntity? = userDao.login(username, password)
    
    suspend fun insert(user: UserEntity): Long = userDao.insert(user)
    
    suspend fun update(user: UserEntity) = userDao.update(user)
    
    suspend fun delete(user: UserEntity) = userDao.delete(user)
    
    suspend fun getCountByRole(role: String): Int = userDao.getCountByRole(role)
    
    suspend fun updatePassword(userId: Long, newPassword: String) = userDao.updatePassword(userId, newPassword)
}
