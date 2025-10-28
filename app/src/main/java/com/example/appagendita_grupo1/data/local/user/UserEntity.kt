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

    val password: String
    // Nota: No guardamos el "confirmPassword",
    // ya que ese campo solo se usa para validación en la UI al momento del registro.
)