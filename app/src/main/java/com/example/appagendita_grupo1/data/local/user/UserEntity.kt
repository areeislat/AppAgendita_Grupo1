package com.example.appagendita_grupo1.data.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Definimos la tabla "users"
@Entity(
    tableName = "users",
    // Creamos un índice único para el email.
    // Esto es crucial: evita que dos usuarios se registren con el mismo email.
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,

    @ColumnInfo(name = "email") // Especificamos el nombre de la columna (buena práctica)
    val email: String,

    // Password should be hashed using BCrypt
    val password: String,
    
    // Session management fields
    @ColumnInfo(name = "session_token")
    val sessionToken: String? = null,
    
    @ColumnInfo(name = "session_created_at")
    val sessionCreatedAt: Long? = null,
    
    @ColumnInfo(name = "session_expires_at")
    val sessionExpiresAt: Long? = null,
    
    // Account status
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    // Timestamps
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
    // Nota: No guardamos el "confirmPassword",
    // ya que ese campo solo se usa para validación en la UI al momento del registro.
)