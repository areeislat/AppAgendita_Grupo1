package com.example.appagendita_grupo1.viewmodel

data class AddEventState(
    val eventName: String = "",
    val eventNameError: String? = null,
    val description: String = "",
    val location: String = "",
    val eventDate: String = "",
    val eventDateError: String? = null,
    val startTime: String = "",
    val startTimeError: String? = null,
    val endTime: String = "",
    val isLoading: Boolean = false,
    val isEventSaved: Boolean = false,
    val saveError: String? = null
)
