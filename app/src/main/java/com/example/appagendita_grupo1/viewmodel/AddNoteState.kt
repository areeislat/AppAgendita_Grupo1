package com.example.appagendita_grupo1.viewmodel

import android.net.Uri // Importa Uri
data class AddNoteState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val imageUri: Uri? = null // se añade este campo para importar uri de foto
)
