package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.model.LoginState
import com.example.appagendita_grupo1.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null, generalError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null, generalError = null)
    }

    fun onLoginClick() {
        if (!validate()) return

        state = state.copy(isLoading = true)

        viewModelScope.launch {
            // Llamada al repositorio (que ahora llama a la API)
            when (val result = repository.login(state.email.trim(), state.password)) {
                is UserRepository.AuthResult.Success -> {
                    val loginResponse = result.response

                    // Guardar sesión en SharedPreferences
                    sessionManager.saveSession(
                        userId = loginResponse.user.id, // UUID String
                        userEmail = loginResponse.user.email,
                        userName = loginResponse.user.username,
                        authToken = loginResponse.token
                    )

                    state = state.copy(loginSuccess = true, isLoading = false)
                }
                is UserRepository.AuthResult.Error -> {
                    state = state.copy(
                        generalError = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (state.email.isBlank()) {
            state = state.copy(emailError = "El email es requerido")
            isValid = false
        }
        if (state.password.isBlank()) {
            state = state.copy(passwordError = "La contraseña es requerida")
            isValid = false
        }
        return isValid
    }
}