package com.example.appagendita_grupo1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddTeamViewModel : ViewModel() {
    var state by mutableStateOf(AddTeamState())
        private set

    fun onTeamNameChange(teamName: String) {
        state = state.copy(teamName = teamName, teamNameError = null)
    }

    fun onTeamTypeChange(teamType: String) {
        state = state.copy(teamType = teamType)
    }

    fun validate(): Boolean {
        var isValid = true
        if (state.teamName.isBlank()) {
            state = state.copy(teamNameError = "El nombre del equipo no puede estar vacío")
            isValid = false
        }
        return isValid
    }
}
