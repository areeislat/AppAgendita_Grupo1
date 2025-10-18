package com.example.appagendita_grupo1.viewmodel

data class AddEventState(
    val eventName: String = "",
    val eventNameError: String? = null,
    val eventDate: String = "",
    val eventDateError: String? = null,
    val startTime: String = "",
    val startTimeError: String? = null,
    val endTime: String = ""
)
