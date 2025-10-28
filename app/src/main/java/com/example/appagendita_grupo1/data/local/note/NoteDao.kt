package com.example.appagendita_grupo1.data.local.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow // Importar Flow para datos reactivos

@Dao
interface NoteDao {

    // Inserta una nueva nota. Si hay conflicto (mismo ID), reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long // Devuelve el ID generado

    // Actualiza una nota existente
    @Update
    suspend fun update(note: NoteEntity)

    // Elimina una nota
    @Delete
    suspend fun delete(note: NoteEntity)

    // Obtiene todas las notas ordenadas por ID descendente (las más nuevas primero)
    // Devuelve un Flow para que la UI reaccione a cambios automáticamente
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    // Obtiene notas filtradas por userId
    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY id DESC")
    fun getNotesByUserId(userId: Long): Flow<List<NoteEntity>>

    // Obtiene una nota específica por su ID
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): NoteEntity?

    // (Opcional) Cuenta el total de notas
    @Query("SELECT COUNT(*) FROM notes")
    suspend fun count(): Int
}