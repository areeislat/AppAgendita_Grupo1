package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appagendita_grupo1.data.repository.NoteRepository

// Factory para AddNoteViewModel
class AddNoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNoteViewModel(repository) as T // Pasa el repositorio al constructor
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Puedes añadir aquí más factories para otros ViewModels que necesiten repositorios
// class NoteListViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory { ... }