package com.pisroster.app.data.dao

import androidx.room.*
import com.pisroster.app.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
    
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): UserEntity?
    
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE role = :role")
    fun getUsersByRole(role: String): Flow<List<UserEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long
    
    @Update
    suspend fun update(user: UserEntity)
    
    @Delete
    suspend fun delete(user: UserEntity)
    
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT COUNT(*) FROM users WHERE role = :role")
    suspend fun getCountByRole(role: String): Int
}
