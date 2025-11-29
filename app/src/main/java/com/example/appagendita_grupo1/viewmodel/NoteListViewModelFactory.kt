package com.example.appagendita_grupo1.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appagendita_grupo1.data.repository.NoteRepository
import javax.inject.Inject


class NoteListViewModelFactory @Inject constructor(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
