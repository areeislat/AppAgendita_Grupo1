package com.example.appagendita_grupo1.viewmodel

import com.example.appagendita_grupo1.data.local.note.NoteEntity

// Esta clase de estado simplemente contendrá la lista de notas
data class NoteListState(
    val notes: List<NoteEntity> = emptyList()
)