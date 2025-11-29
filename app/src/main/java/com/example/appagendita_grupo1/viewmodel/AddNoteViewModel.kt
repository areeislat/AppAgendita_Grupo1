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

        viewModelScope.launch {
            try {
                repository.addNote(
                    title = state.title.trim(),
                    description = state.description.trim(),
                    imageUri = state.imageUri?.toString()
                )
                state = state.copy(isNoteSaved = true)
            } catch (e: Exception) {
                // Handle critical errors if the repository doesn't
                e.printStackTrace()
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (state.title.isBlank()) {
            state = state.copy(titleError = "El título no puede estar vacío")
            isValid = false
        }
        return isValid
    }

    // Dentro de la clase AddNoteViewModel

    fun onSaveClicked(onSuccess: () -> Unit) {
        // Llama a la función privada `validate`
        if (validate()) {
            // Si la validación es correcta, llama a `onSaveNote`
            onSaveNote()
            // Y finalmente, ejecuta la acción `onSuccess` (que es `onNoteSaved` en la pantalla)
            onSuccess()
        }
    }

data class AddNoteState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val imageUri: Uri? = null,
    val isNoteSaved: Boolean = false
) 
}