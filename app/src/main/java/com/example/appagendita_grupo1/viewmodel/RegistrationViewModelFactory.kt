package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.utils.SessionManager

class RegistrationViewModelFactory(
    private val userRepository: UserRepository,
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistrationViewModel(userRepository, apiService, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
