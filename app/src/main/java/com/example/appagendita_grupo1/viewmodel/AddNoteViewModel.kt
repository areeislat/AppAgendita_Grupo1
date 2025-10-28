package com.example.appagendita_grupo1.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // <-- 1. IMPORTAR
import com.example.appagendita_grupo1.data.local.note.NoteEntity // <-- 2. IMPORTAR
import com.example.appagendita_grupo1.data.repository.NoteRepository // <-- 3. IMPORTAR
import kotlinx.coroutines.launch // <-- 4. IMPORTAR

// --- CAMBIO 1: Añadir el repositorio en el constructor ---
class AddNoteViewModel(private val repository: NoteRepository) : ViewModel() {
    var state by mutableStateOf(AddNoteState())
        private set

    fun onTitleChange(title: String) {
        state = state.copy(title = title, titleError = null)
    }

    fun onDescriptionChange(description: String) {
        // Mantenemos la lógica de limpiar el error si el usuario escribe
        state = state.copy(description = description, descriptionError = null)
    }

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
        // --- CAMBIO 2: Lógica de guardado real ---

        // Si la validación falla, detenemos la ejecución
        if (!validate()) return

        // 1. Crear la Entidad de Nota a partir del estado actual
        val noteToSave = NoteEntity(
            title = state.title.trim(),
            description = state.description.trim(),
            imageUri = state.imageUri?.toString() // Convertir la Uri a String para guardarla
        )

        // 2. Lanzar una corrutina (en el viewModelScope) para insertar en la BD
        //    Esto asegura que la operación (I/O) no bloquee el hilo principal (UI)
        viewModelScope.launch {
            repository.addNote(noteToSave)
            println("¡Nota guardada exitosamente en Room!: $noteToSave")
        }
        // --- FIN CAMBIO 2 ---
    }

    fun validate(): Boolean {
        var isValid = true
        if (state.title.isBlank()) {
            state = state.copy(titleError = "El título no puede estar vacío")
            isValid = false
        }

        // --- CAMBIO 3: La descripción ya no es obligatoria ---
        // Una nota puede ser solo un título y una foto, o solo una descripción.
        // Pero para este caso, solo exigiremos el título.
        /*
        if (state.description.isBlank()) {
            state = state.copy(descriptionError = "La descripción no puede estar vacía")
            isValid = false
        }
        */
        // --- FIN CAMBIO 3 ---

        return isValid
    }

}