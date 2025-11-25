package com.example.appagendita_grupo1.data.remote.response

data class NoteResponse(
    val id: String,
    val title: String,
    val description: String,
    val imageUri: String?,
    val userId: String,
    val createdAt: String?, // Recibimos la fecha como String ISO
    val updatedAt: String?
)