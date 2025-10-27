package com.example.appagendita_grupo1.viewmodel

import android.net.Uri // <- 1. Importar Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddNoteViewModel : ViewModel() {
    var state by mutableStateOf(AddNoteState())
        private set

    fun onTitleChange(title: String) {
        state = state.copy(title = title, titleError = null)
    }

    fun onDescriptionChange(description: String) {
        state = state.copy(description = description, descriptionError = null)
    }

    // --- INICIO DE CAMBIOS ---

    /**
     * Actualiza el estado con la Uri de la foto que se acaba de tomar.
     */
    fun onPhotoTaken(uri: Uri?) {
        state = state.copy(imageUri = uri)
    }

    /**
     * Lógica para guardar la nota.
     * (Esta se llamará antes de navegar hacia atrás)
     */
    fun onSaveNote() {
        // Aquí puedes ver el estado completo, incluida la URI
        println("Guardando Nota: State=${state}")

        // Aquí iría la lógica futura para guardar en la base de datos:
        // val note = Note(
        //     title = state.title,
        //     description = state.description,
        //     imageUri = state.imageUri.toString() // Guardar la Uri como String
        // )
        // repository.insertNote(note)
    }

    // --- FIN DE CAMBIOS ---

    fun validate(): Boolean {
        var isValid = true
        if (state.title.isBlank()) {
            state = state.copy(titleError = "El título no puede estar vacío")
            isValid = false
        }
        if (state.description.isBlank()) {
            state = state.copy(descriptionError = "La descripción no puede estar vacía")
            isValid = false
        }
        return isValid
    }

}