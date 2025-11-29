package com.example.appagendita_grupo1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    var state by mutableStateOf(AddEventState())
        private set

    fun onEventNameChange(eventName: String) {
        state = state.copy(eventName = eventName, eventNameError = null)
    }

    fun onEventDescriptionChange(description: String) {
        state = state.copy(description = description)
    }

    fun onEventLocationChange(location: String) {
        state = state.copy(location = location)
    }

    fun onEventDateChange(eventDate: String) {
        state = state.copy(eventDate = eventDate, eventDateError = null)
    }

    fun onStartTimeChange(startTime: String) {
        state = state.copy(startTime = startTime, startTimeError = null)
    }

    fun onEndTimeChange(endTime: String) {
        state = state.copy(endTime = endTime)
    }

    fun onSaveEvent() {
        if (!validate()) return

        state = state.copy(isLoading = true, saveError = null)

        viewModelScope.launch {
            try {
                // Parse the date and time
                val eventDate = LocalDate.parse(state.eventDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val eventTime = if (state.startTime.isNotBlank()) {
                    LocalTime.parse(state.startTime, DateTimeFormatter.ofPattern("HH:mm"))
                } else {
                    LocalTime.now()
                }
                
                val eventDateTime = LocalDateTime.of(eventDate, eventTime)

                // Save the event
                eventRepository.addEvent(
                    title = state.eventName.trim(),
                    description = if (state.description.isBlank()) null else state.description.trim(),
                    location = if (state.location.isBlank()) null else state.location.trim(),
                    eventTimestamp = eventDateTime
                )

                state = state.copy(
                    isLoading = false,
                    isEventSaved = true
                )
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    saveError = "Error al guardar evento: ${e.message}"
                )
            }
        }
    }

    fun validate(): Boolean {
        var isValid = true
        if (state.eventName.isBlank()) {
            state = state.copy(eventNameError = "El nombre del evento no puede estar vacío")
            isValid = false
        }
        if (state.eventDate.isBlank()) {
            state = state.copy(eventDateError = "La fecha del evento no puede estar vacía")
            isValid = false
        }
        if (state.startTime.isBlank()) {
            state = state.copy(startTimeError = "La hora de inicio no puede estar vacía")
            isValid = false
        }
        return isValid
    }
}
