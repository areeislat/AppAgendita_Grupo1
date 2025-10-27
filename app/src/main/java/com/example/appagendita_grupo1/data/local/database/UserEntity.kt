package com.example.appagendita_grupo1.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    fun hasValidCredentials(): Boolean = username.isNotBlank() && email.isNotBlank() && passwordHash.isNotBlank()

    fun matchesPassword(rawHash: String): Boolean = passwordHash == rawHash

    fun normalizedEmail(): String = email.trim().lowercase()
}
