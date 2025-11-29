package com.example.appagendita_grupo1.data.repository

import android.util.Log
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.NoteRequest
import com.example.appagendita_grupo1.data.remote.response.NoteResponse
import com.example.appagendita_grupo1.di.NoteApi
import com.example.appagendita_grupo1.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import javax.inject.Named

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    @Named("noteApi") private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    /**
     * Obtiene notas combinando API y Base de Datos Local.
     * Estrategia: Emitir local -> Intentar API -> Emitir combinado (Local + API)
     */
    fun getAllNotes(): Flow<List<NoteEntity>> = flow {
        // 1. Obtener UUID del usuario (String)
        val userId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("Usuario no logueado")

        // 2. Emitir datos locales inmediatamente (Para que la UI cargue rápido)
        // Ahora 'getNotesByUserId' acepta String, así que esto funcionará perfecto.
        val localNotes = noteDao.getNotesByUserId(userId).first()
        emit(localNotes)

        try {
            // 3. Intentar obtener datos de la red
            val response = apiService.getUserNotes(userId)

            if (response.isSuccessful && response.body() != null) {
                val networkNotes = response.body()!!

                // 4. Mapear notas de red a Entidades Locales (solo para visualización)
                val mappedNetworkNotes = networkNotes.map { apiNote ->
                    apiNote.toNoteEntity(userId)
                }

                // 5. Combinar: Notas Locales (pendientes de subir) + Notas de Red
                // (Aquí podrías filtrar duplicados si implementamos lógica de sync más compleja)
                val combinedNotes = localNotes + mappedNetworkNotes
                emit(combinedNotes)
            } else {
                Log.e("NoteRepository", "Error fetching notes: ${response.code()}")
            }
        } catch (e: Exception) {
            // Si falla la red, ya emitimos los datos locales, así que el usuario ve sus pendientes.
            Log.e("NoteRepository", "Network error: ${e.message}")
        }
    }

    /**
     * Lógica Store-and-Forward:
     * Intenta subir a la API. Si falla (offline), guarda en local.
     */
    suspend fun addNote(title: String, description: String, imageUri: String?) {
        val userId = sessionManager.getCurrentUserId() ?: return

        // Creamos la entidad temporal (para guardar en local si falla la red)
        val tempNote = NoteEntity(
            title = title,
            description = description,
            imageUri = imageUri,
            userId = userId // UUID String
        )

        try {
            // 1. Crear Request para la API
            val request = NoteRequest(
                title = title,
                description = description,
                imageUri = imageUri,
                userId = userId
            )

            // 2. Intentar enviar a la API
            val response = apiService.createNote(request)

            if (response.isSuccessful) {
                // Éxito: La nota ya está en la nube.
                // No la guardamos en local para no duplicar (se descargará al refrescar).
                Log.d("NoteRepository", "Nota subida exitosamente a la nube")
            } else {
                // Fallo de servidor (ej. 500): Guardar localmente para reintentar luego
                Log.w("NoteRepository", "Error en servidor, guardando localmente")
                noteDao.insert(tempNote)
            }

        } catch (e: Exception) {
            // 3. Fallo de Red (Offline): Guardar localmente
            Log.w("NoteRepository", "Sin conexión, guardando nota localmente: ${e.message}")
            noteDao.insert(tempNote)
        }
    }

    /**
     * Elimina una nota.
     * Por ahora solo eliminamos localmente.
     * TODO: Implementar borrado en API si la nota tiene un ID remoto.
     */
    suspend fun deleteNote(note: NoteEntity) {
        noteDao.delete(note)
    }

    // Helper para convertir respuesta de API a Entidad visual
    private fun NoteResponse.toNoteEntity(localUserId: String): NoteEntity {
        return NoteEntity(
            // Generamos un ID numérico temporal basado en el hash del UUID para que Room/LazyColumn no se quejen.
            // Nota: Esto es solo para visualización en memoria, no se guarda en la BD con este ID.
            id = this.id.hashCode().toLong(),
            title = this.title,
            description = this.description,
            imageUri = this.imageUri,
            userId = localUserId
        )
    }
}