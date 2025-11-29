package com.example.appagendita_grupo1.data.repository

import android.util.Log
import com.example.appagendita_grupo1.data.local.event.EventDao
import com.example.appagendita_grupo1.data.local.event.EventEntity
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.EventRequest
import com.example.appagendita_grupo1.data.remote.response.EventResponse
import com.example.appagendita_grupo1.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import javax.inject.Named

class EventRepository @Inject constructor(
    private val eventDao: EventDao,
    @Named("eventApi") private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    fun getAllEvents(): Flow<List<EventEntity>> = flow {
        val ownerId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("Usuario no logueado")

        val localEvents = eventDao.getEventsByOwner(ownerId).first()
        emit(localEvents)

        try {
            val response = apiService.getUserEvents(ownerId)

            if (response.isSuccessful && response.body() != null) {
                val networkEvents = response.body()!!

                val mappedNetworkEvents = networkEvents.map { apiEvent ->
                    apiEvent.toEventEntity()
                }

                val combinedEvents = localEvents + mappedNetworkEvents
                emit(combinedEvents)
            } else {
                Log.e("EventRepository", "Error fetching events: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("EventRepository", "Network error: ${e.message}")
        }
    }

    suspend fun addEvent(title: String, description: String?, location: String?, eventTimestamp: LocalDateTime) {
        val ownerId = sessionManager.getCurrentUserId() ?: return

        val tempEvent = EventEntity(
            id = "", // Se genera en el backend
            ownerId = ownerId,
            title = title,
            description = description,
            eventTimestamp = eventTimestamp,
            location = location,
            createdAt = LocalDateTime.now(),
            updatedAt = null
        )

        try {
            val request = EventRequest(
                ownerId = ownerId,
                title = title,
                description = description,
                eventTimestamp = eventTimestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                location = location
            )

            val response = apiService.createEvent(request)

            if (response.isSuccessful) {
                Log.d("EventRepository", "Evento subido exitosamente a la nube")
            } else {
                Log.w("EventRepository", "Error en servidor, guardando localmente")
                eventDao.insertEvent(tempEvent)
            }

        } catch (e: Exception) {
            Log.w("EventRepository", "Sin conexión, guardando evento localmente: ${e.message}")
            eventDao.insertEvent(tempEvent)
        }
    }

    suspend fun deleteEvent(event: EventEntity) {
        // TODO: Implementar borrado en API
        //eventDao.deleteEvent(event)
    }

    private fun EventResponse.toEventEntity(): EventEntity {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return EventEntity(
            id = this.id,
            ownerId = this.ownerId,
            title = this.title,
            description = this.description,
            eventTimestamp = LocalDateTime.parse(this.eventTimestamp, formatter),
            location = this.location,
            createdAt = LocalDateTime.parse(this.createdAt, formatter),
            updatedAt = this.updatedAt?.let { LocalDateTime.parse(it, formatter) }
        )
    }
}
