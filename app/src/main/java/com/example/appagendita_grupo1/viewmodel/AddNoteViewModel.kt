package com.example.appagendita_grupo1.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    var state by mutableStateOf(AddNoteState())
        private set

    fun onTitleChange(title: String) {
        state = state.copy(title = title, titleError = null)
    }

    fun onDescriptionChange(description: String) {
        state = state.copy(description = description, descriptionError = null)
    }

    fun onPhotoTaken(uri: Uri?) {
        state = state.copy(imageUri = uri)
    }

    fun onSaveNote() {
        if (!validate()) return

        // Lanzar corrutina
        viewModelScope.launch {
            try {
                // CAMBIO: Llamamos al método del repositorio con los parámetros individuales
                // Convertimos la URI a String si existe
                repository.addNote(
                    title = state.title.trim(),
                    description = state.description.trim(),
                    imageUri = state.imageUri?.toString()
                )
                // Aquí podrías emitir un evento de "Guardado Exitoso" si quisieras navegar atrás automáticamente
            } catch (e: Exception) {
                // Manejo de error si algo falla críticamente (aunque el repo ya captura la mayoría)
                e.printStackTrace()
            }
        }
    }

    fun validate(): Boolean {
        var isValid = true
        if (state.title.isBlank()) {
            state = state.copy(titleError = "El título no puede estar vacío")
            isValid = false
        }
        // La descripción es opcional
        return isValid
    }
}