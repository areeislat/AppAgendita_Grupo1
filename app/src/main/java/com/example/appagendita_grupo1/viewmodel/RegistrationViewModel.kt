package com.example.appagendita_grupo1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword)
    }

    fun validate(): Boolean {
        state = state.copy(
            emailError = if (state.email.isBlank()) "El email no puede estar vacío" else null,
            passwordError = if (state.password.isBlank()) "La contraseña no puede estar vacía" else null,
            confirmPasswordError = if (state.password != state.confirmPassword) "Las contraseñas no coinciden" else null
        )
        return state.emailError == null && state.passwordError == null && state.confirmPasswordError == null
    }
}
