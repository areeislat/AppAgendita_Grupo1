package com.example.appagendita_grupo1.data.remote

// Imports existentes
import com.example.appagendita_grupo1.data.remote.request.RegisterRequest
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.data.remote.response.UserResponse

// --- NUEVOS IMPORTS PARA NOTAS ---
import com.example.appagendita_grupo1.data.remote.request.NoteRequest
import com.example.appagendita_grupo1.data.remote.response.NoteResponse

import com.example.appagendita_grupo1.data.remote.request.LoginRequest
import com.example.appagendita_grupo1.data.remote.response.LoginResponse

// --- NUEVOS IMPORTS PARA EVENTOS ---
import com.example.appagendita_grupo1.data.remote.request.EventRequest
import com.example.appagendita_grupo1.data.remote.response.EventResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // --- ENDPOINTS EXISTENTES ---
    @POST("api/tasks")
    suspend fun createTask(@Body taskRequest: TaskRequest): Response<Unit>

    @POST("api/users")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<UserResponse>

    @POST("api/events")
    suspend fun createEvent(@Body eventRequest: EventRequest): Response<EventResponse>


    // --- NUEVOS ENDPOINTS PARA NOTAS ---

    // 1. Obtener todas las notas del usuario
    @GET("api/notes/user/{userId}")
    suspend fun getUserNotes(@Path("userId") userId: String): Response<List<NoteResponse>>

    // 2. Crear una nueva nota
    @POST("api/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    // 3. Eliminar una nota (requiere ambos IDs por seguridad del backend)
    @DELETE("api/notes/{noteId}/user/{userId}")
    suspend fun deleteNote(
        @Path("noteId") noteId: String,
        @Path("userId") userId: String
    ): Response<Map<String, String>>

    // 4. Actualizar una nota
    // Nota: Reutilizamos NoteRequest por simplicidad. El backend ignorará el campo 'userId' del cuerpo si sobra.
    @PUT("api/notes/{noteId}/user/{userId}")
    suspend fun updateNote(
        @Path("noteId") noteId: String,
        @Path("userId") userId: String,
        @Body noteRequest: NoteRequest
    ): Response<NoteResponse>

    @POST("api/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // --- NUEVOS ENDPOINTS PARA EVENTOS ---

    @GET("api/events/user/{ownerId}")
    suspend fun getUserEvents(@Path("ownerId") ownerId: String): Response<List<EventResponse>>



}
