package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword, confirmPasswordError = null)
    }

    fun validate(): Boolean {
        var hasError = false
        val email = state.email
        val password = state.password
        val confirmPassword = state.confirmPassword

        if (email.isBlank()) {
            state = state.copy(emailError = "El email no puede estar vacío")
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            state = state.copy(emailError = "Por favor, ingresa un email válido")
            hasError = true
        }

        if (password.isBlank()) {
            state = state.copy(passwordError = "La contraseña no puede estar vacía")
            hasError = true
        } else if (password.length < 6) {
            state = state.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
            hasError = true
        }

        if (confirmPassword.isBlank()) {
            state = state.copy(confirmPasswordError = "Por favor, confirma tu contraseña")
            hasError = true
        } else if (password != confirmPassword) {
            state = state.copy(confirmPasswordError = "Las contraseñas no coinciden")
            hasError = true
        }

        return !hasError
    }
}
