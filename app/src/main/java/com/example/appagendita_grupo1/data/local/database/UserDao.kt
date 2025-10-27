package com.example.appagendita_grupo1.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY created_at DESC")
    fun observeUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun findById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clear()

    @Transaction
    suspend fun upsert(user: UserEntity): Long {
        val existing = findByEmail(user.normalizedEmail())
        return if (existing == null) {
            insert(user.copy(email = user.normalizedEmail()))
        } else {
            val updated = user.copy(
                id = existing.id,
                createdAt = existing.createdAt,
                email = user.normalizedEmail()
            )
            update(updated)
            existing.id
        }
    }
}
