package com.example.appagendita_grupo1.model

data class RegistrationState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,

    /**
     * Se pondrá en 'true' cuando el VM
     * haya guardado al usuario en la BD exitosamente.
     * La Vista (Screen) observará este campo para navegar.
     */
    val registrationSuccess: Boolean = false,
    
    /**
     * Para errores que no son de un campo específico
     */
    val generalError: String? = null
)