package com.example.appagendita_grupo1.data.local.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes") // Define el nombre de la tabla
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) // Clave primaria autoincremental
    val id: Long = 0L,

    val title: String,
    val description: String,
    val imageUri: String?, // Guardamos la Uri como String (puede ser nulo)
    val userId: Long = 0L // Foreign key to associate note with user
    // Puedes añadir más campos aquí (fecha de creación, etc.) si lo necesitas
)