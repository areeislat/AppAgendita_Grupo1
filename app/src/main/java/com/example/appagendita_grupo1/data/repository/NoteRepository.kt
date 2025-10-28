package com.example.appagendita_grupo1.data.repository

import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.utils.SessionManager
import kotlinx.coroutines.flow.Flow

// El repositorio necesita el DAO para acceder a los datos
class NoteRepository(
    private val noteDao: NoteDao,
    private val sessionManager: SessionManager
) {

    // Obtiene el Flow de todas las notas del usuario actual
    // Throws IllegalStateException if no active session
    fun getAllNotes(): Flow<List<NoteEntity>> {
        val userId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("No active session. User must be logged in to access notes.")
        return noteDao.getNotesByUserId(userId)
    }

    // Inserta una nota usando el DAO (operación suspendida)
    // Automatically associates note with current user
    suspend fun addNote(note: NoteEntity) {
        val userId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("No active session. User must be logged in to add notes.")
        
        // Ensure the note is associated with the current user
        val noteWithUser = note.copy(userId = userId)
        noteDao.insert(noteWithUser)
    }

    // Obtiene una nota por ID (operación suspendida)
    // Only returns the note if it belongs to the current user
    suspend fun getNoteById(noteId: Long): NoteEntity? {
        val userId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("No active session. User must be logged in to access notes.")
        
        val note = noteDao.getNoteById(noteId)
        // Verify the note belongs to the current user
        return if (note?.userId == userId) note else null
    }

    // Actualiza una nota (operación suspendida)
    // Only allows updating notes that belong to the current user
    suspend fun updateNote(note: NoteEntity) {
        val userId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("No active session. User must be logged in to update notes.")
        
        // Verify the note belongs to the current user
        if (note.userId != userId) {
            throw IllegalArgumentException("Cannot update note that belongs to another user.")
        }
        noteDao.update(note)
    }

    // Elimina una nota (operación suspendida)
    // Only allows deleting notes that belong to the current user
    suspend fun deleteNote(note: NoteEntity) {
        val userId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("No active session. User must be logged in to delete notes.")
        
        // Verify the note belongs to the current user
        if (note.userId != userId) {
            throw IllegalArgumentException("Cannot delete note that belongs to another user.")
        }
        noteDao.delete(note)
    }
}