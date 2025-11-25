package com.example.appagendita_grupo1.data.local.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L, // El ID local de la nota sigue siendo Long (autogenerado por Room)

    val title: String,
    val description: String,
    val imageUri: String?,

    // --- CAMBIO: userId ahora es String ---
    // Esto nos permite guardar el UUID que viene del backend (ej: "550e8400-e29b...")
    val userId: String
)