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

    // --- INICIO DE CAMBIOS ---
    /**
     * Se pondrá en 'true' cuando el VM
     * haya guardado al usuario en la BD exitosamente.
     * La Vista (Screen) observará este campo para navegar.
     */
    val registrationSuccess: Boolean = false,
    
    /**
     * Error general que no está asociado a un campo específico
     */
    val generalError: String? = null
    // --- FIN DE CAMBIOS ---
)