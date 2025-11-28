package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.model.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var state by mutableStateOf(RegistrationState())
        private set

    fun onNameChange(name: String) { state = state.copy(name = name, nameError = null) }
    fun onEmailChange(email: String) { state = state.copy(email = email, emailError = null) }
    fun onPasswordChange(password: String) { state = state.copy(password = password, passwordError = null) }
    fun onConfirmPasswordChange(confirm: String) { state = state.copy(confirmPassword = confirm, confirmPasswordError = null) }

    fun onRegisterClick() {
        if (!validate()) return

        state = state.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = repository.registerUser(
                name = state.name.trim(),
                email = state.email.trim(),
                password = state.password
            )) {
                is UserRepository.RegistrationResult.Success -> {
                    state = state.copy(registrationSuccess = true, isLoading = false)
                }
                is UserRepository.RegistrationResult.Error -> {
                    // Mostrar error (podrías agregar un campo generalError al estado si no lo tienes)
                    // Por ahora lo ponemos en emailError como fallback
                    state = state.copy(
                        emailError = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        // ... (Tu lógica de validación existente se mantiene igual) ...
        if (state.name.isBlank()) {
            state = state.copy(nameError = "Nombre requerido")
            isValid = false
        }
        // ... resto de validaciones ...
        return isValid
    }
}