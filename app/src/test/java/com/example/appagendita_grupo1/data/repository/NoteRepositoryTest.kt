package com.example.appagendita_grupo1.data.repository

import android.util.Log
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.NoteRequest
import com.example.appagendita_grupo1.data.remote.response.NoteResponse
import com.example.appagendita_grupo1.utils.SessionManager
import io.kotest.core.spec.style.StringSpec
import io.mockk.*
import retrofit2.Response

class NoteRepositoryTest : StringSpec({

    // 1. Mocks (Objetos simulados)
    val noteDao = mockk<NoteDao>(relaxed = true) // relaxed=true permite llamar métodos sin definir comportamiento previo
    val apiService = mockk<ApiService>()
    val sessionManager = mockk<SessionManager>()

    // 2. Objeto a probar (SUT - System Under Test)
    val repository = NoteRepository(noteDao, apiService, sessionManager)

    // Configuración previa a cada test
    beforeTest {
        clearAllMocks()

        // Mockear Log de Android para evitar errores "Method d in android.util.Log not mocked"
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    // --- CASO 1: SUBIDA EXITOSA ---
    "addNote debería subir a la API y NO guardar en local si la respuesta es exitosa" {
        // Arrange (Preparar)
        val userId = "user-uuid-123"
        val title = "Nota de prueba"
        val description = "Descripción"
        val imageUri = null

        // Simulamos que hay usuario logueado
        every { sessionManager.getCurrentUserId() } returns userId

        // Simulamos respuesta exitosa de la API (200 OK)
        val mockResponse = Response.success(
            NoteResponse(
                id = "note-id-1",
                title = title,
                description = description,
                imageUri = imageUri,
                userId = userId,
                createdAt = "2024-01-01",
                updatedAt = "2024-01-01"
            )
        )
        // 'coEvery' se usa para funciones suspendidas (coroutines)
        coEvery { apiService.createNote(any()) } returns mockResponse

        // Act (Ejecutar)
        repository.addNote(title, description, imageUri)

        // Assert (Verificar)
        // Verificamos que SE LLAMÓ a la API
        coVerify(exactly = 1) {
            apiService.createNote(match { it.title == title && it.userId == userId })
        }

        // Verificamos que NO se llamó al Dao local (porque se subió con éxito)
        // (Según tu lógica actual: "Si response.isSuccessful -> Log.d (no insert)")
        coVerify(exactly = 0) { noteDao.insert(any()) }
    }

    // --- CASO 2: FALLO DE SERVIDOR (ERROR 500) ---
    "addNote debería guardar en LOCAL si la API devuelve error" {
        // Arrange
        val userId = "user-uuid-123"
        every { sessionManager.getCurrentUserId() } returns userId

        // Simulamos respuesta de error (500 Internal Server Error)
        val errorResponse = Response.error<NoteResponse>(500, okhttp3.ResponseBody.create(null, "Error"))
        coEvery { apiService.createNote(any()) } returns errorResponse

        // Act
        repository.addNote("Título", "Desc", null)

        // Assert
        // Verificamos que SÍ se llamó al Dao para guardar localmente
        coVerify(exactly = 1) {
            noteDao.insert(match { it.title == "Título" && it.userId == userId })
        }
    }

    // --- CASO 3: SIN INTERNET (EXCEPCIÓN) ---
    "addNote debería guardar en LOCAL si la API lanza excepción (Offline)" {
        // Arrange
        val userId = "user-uuid-123"
        every { sessionManager.getCurrentUserId() } returns userId

        // Simulamos una excepción de red (como si no hubiera internet)
        coEvery { apiService.createNote(any()) } throws RuntimeException("No internet")

        // Act
        repository.addNote("Nota Offline", "Desc", null)

        // Assert
        // Verificamos que ante la excepción, el catch captura y guarda en local
        coVerify(exactly = 1) {
            noteDao.insert(match { it.title == "Nota Offline" })
        }
    }
})