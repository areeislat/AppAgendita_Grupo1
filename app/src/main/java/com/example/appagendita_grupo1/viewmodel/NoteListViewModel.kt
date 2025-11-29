package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.viewmodel.NoteListState
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

    val state: StateFlow<NoteListState> = repository.getAllNotes()
        .map { notes ->
            NoteListState(notes = notes)
        }
        .catch { e ->
            e.printStackTrace()
            emit(NoteListState(notes = emptyList()))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteListState()
        )
}