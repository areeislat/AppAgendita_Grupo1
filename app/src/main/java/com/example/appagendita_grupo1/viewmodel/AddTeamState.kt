package com.example.appagendita_grupo1.viewmodel

data class AddTeamState(
    val teamName: String = "",
    val teamNameError: String? = null,
    val teamType: String = "Privado"
)
