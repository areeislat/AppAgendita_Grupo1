package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteListViewModel(private val repository: NoteRepository) : ViewModel() {

    // Esta es la forma moderna de recolectar un Flow en un ViewModel
    // Transforma el Flow<List<NoteEntity>> en un StateFlow<NoteListState>
    val state: StateFlow<NoteListState> = repository.getAllNotes()
        .map { notes ->
            // Mapea la lista de entidades al estado de la UI
            NoteListState(notes = notes)
        }
        .stateIn(
            // El scope en el que se ejecutará la corrutina
            scope = viewModelScope,
            // Inicia la recolección 5 segundos después de que el último subscriptor desaparezca
            // (evita reiniciar en cambios de configuración como rotar la pantalla)
            started = SharingStarted.WhileSubscribed(5000L),
            // El estado inicial mientras se espera la primera emisión del Flow
            initialValue = NoteListState()
        )
}