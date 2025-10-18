package com.example.appagendita_grupo1.viewmodel

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
