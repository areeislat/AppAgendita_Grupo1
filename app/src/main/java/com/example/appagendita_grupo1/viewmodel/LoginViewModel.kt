package com.example.appagendita_grupo1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.model.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null)
    }

    fun login(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            // Simulamos una demora de 2 segundos para la validación
            delay(2000)

            var hasError = false
            if (state.email.isBlank()) {
                state = state.copy(emailError = "Por favor, ingresa tu email")
                hasError = true
            }
            if (state.password.isBlank()) {
                state = state.copy(passwordError = "Por favor, ingresa tu contraseña")
                hasError = true
            }

            if (!hasError) {
                onLoginSuccess()
            }

            state = state.copy(isLoading = false)
        }
    }
}
