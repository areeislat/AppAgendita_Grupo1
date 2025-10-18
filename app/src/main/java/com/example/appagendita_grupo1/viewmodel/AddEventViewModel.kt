package com.example.appagendita_grupo1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddEventViewModel : ViewModel() {
    var state by mutableStateOf(AddEventState())
        private set

    fun onEventNameChange(eventName: String) {
        state = state.copy(eventName = eventName, eventNameError = null)
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
