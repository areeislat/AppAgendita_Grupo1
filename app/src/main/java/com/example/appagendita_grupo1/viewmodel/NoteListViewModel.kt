package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    // Convertimos el Flow del repositorio en un StateFlow para la UI
    val state: StateFlow<NoteListState> = repository.getAllNotes()
        .map { notes ->
            // Si recibimos notas, actualizamos el estado
            NoteListState(notes = notes)
        }
        .catch { e ->
            // Si ocurre un error (ej. usuario no logueado), lo capturamos para no crashear
            e.printStackTrace()
            emit(NoteListState(notes = emptyList()))
        }
        .stateIn(
            scope = viewModelScope,
            // Mantiene la suscripción activa 5 segundos después de cerrar la UI (útil para rotación de pantalla)
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteListState()
        )
}