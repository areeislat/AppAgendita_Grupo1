package com.example.appagendita_grupo1.data.remote.request

data class NoteRequest(
    val title: String,
    val description: String,
    val imageUri: String?, // Puede ser nulo si no hay imagen
    val userId: String     // Enviamos el UUID como String
)