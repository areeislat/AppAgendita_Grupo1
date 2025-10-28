package com.example.appagendita_grupo1.data.repository

import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import kotlinx.coroutines.flow.Flow

// El repositorio necesita el DAO para acceder a los datos
class NoteRepository(private val noteDao: NoteDao) {

    // Obtiene el Flow de todas las notas desde el DAO
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    // Inserta una nota usando el DAO (operación suspendida)
    suspend fun addNote(note: NoteEntity) {
        noteDao.insert(note)
    }

    // Obtiene una nota por ID (operación suspendida)
    suspend fun getNoteById(noteId: Long): NoteEntity? {
        return noteDao.getNoteById(noteId)
    }

    // Actualiza una nota (operación suspendida)
    suspend fun updateNote(note: NoteEntity) {
        noteDao.update(note)
    }

    // Elimina una nota (operación suspendida)
    suspend fun deleteNote(note: NoteEntity) {
        noteDao.delete(note)
    }
}