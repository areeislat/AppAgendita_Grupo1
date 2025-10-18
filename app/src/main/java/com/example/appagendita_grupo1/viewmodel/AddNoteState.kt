package com.example.appagendita_grupo1.viewmodel

data class AddNoteState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null
)
