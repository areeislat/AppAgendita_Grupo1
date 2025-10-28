package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appagendita_grupo1.data.repository.NoteRepository
// 1. Importar el nuevo NoteListViewModel
import com.example.appagendita_grupo1.viewmodel.NoteListViewModel

/**
 * Factory para crear AddNoteViewModel.
 * Necesita el NoteRepository para pasárselo al constructor del ViewModel.
 */
class AddNoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNoteViewModel(repository) as T // Pasa el repositorio al constructor
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// --- INICIO DE CAMBIOS ---

/**
 * Factory para crear NoteListViewModel.
 * También necesita el NoteRepository.
 */
class NoteListViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteListViewModel(repository) as T // Pasa el repositorio al constructor
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// --- FIN DE CAMBIOS ---