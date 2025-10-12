package com.example.appagendita_grupo1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appagendita_grupo1.model.RegistrationState

class RegistrationViewModel : ViewModel() {

    var state by mutableStateOf(RegistrationState())
        private set

    fun onNameChange(name: String) {
        state = state.copy(name = name, nameError = null)
    }

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null)
    }

    fun validate(): Boolean {
        var hasError = false
        if (state.name.isBlank()) {
            state = state.copy(nameError = "Please enter your name")
            hasError = true
        }
        if (state.email.isBlank()) {
            state = state.copy(emailError = "Please enter your email")
            hasError = true
        }
        if (state.password.isBlank()) {
            state = state.copy(passwordError = "Please enter your password")
            hasError = true
        }
        return !hasError
    }
}
