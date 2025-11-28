package com.example.appagendita_grupo1.data.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    // Mantenemos el índice único para búsquedas rápidas por email
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    // CAMBIO 1: autoGenerate = false (o quitado).
    // El ID ya no lo genera Room, lo recibimos del Backend (UUID).
    @PrimaryKey
    val id: String,

    val name: String,

    @ColumnInfo(name = "email")
    val email: String

    // CAMBIO 2: Eliminamos 'password', 'sessionToken', etc.
    // Ahora solo guardamos los datos de perfil para mostrar en la UI.
    // La seguridad (Token) vive en SessionManager.
)