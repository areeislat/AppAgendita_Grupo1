package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.viewmodel.NoteListViewModel

// --- INICIO DE CAMBIOS: IMPORTACIONES DE USER ---
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.viewmodel.LoginViewModel
import com.example.appagendita_grupo1.viewmodel.RegistrationViewModel
import com.example.appagendita_grupo1.utils.SessionManager
// --- FIN DE CAMBIOS: IMPORTACIONES DE USER ---


// --- FACTORIES DE NOTAS (EXISTENTES) ---

/**
 * Factory para crear AddNoteViewModel.
 * Necesita el NoteRepository para pasárselo al constructor del ViewModel.
 */
class AddNoteViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory para crear NoteListViewModel.
 * También necesita el NoteRepository.
 */
class NoteListViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// --- INICIO DE CAMBIOS: FACTORIES DE USER ---

/**
 * Factory para crear RegistrationViewModel.
 * Necesita el UserRepository.
 */
class RegistrationViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // (Esto dará error hasta que modifiquemos RegistrationViewModel en el Paso 6)
            return RegistrationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory para crear LoginViewModel.
 * Necesita el UserRepository y SessionManager.
 */
class LoginViewModelFactory(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// --- FIN DE CAMBIOS: FACTORIES DE USER ---