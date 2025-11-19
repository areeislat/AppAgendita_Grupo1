package com.example.appagendita_grupo1.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.utils.SessionManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AddTaskViewModel(
    private val apiService: ApiService
) : ViewModel() {
    var uiState by mutableStateOf(AddTaskUiState())
        private set

    fun onTaskNameChange(value: String) {
        val error = if (value.isBlank()) "El título es obligatorio" else null
        uiState = uiState.copy(taskName = value, taskNameError = error)
    }

    fun onDescriptionChange(value: String) {
        uiState = uiState.copy(description = value, descriptionError = null)
    }

    fun onPrioritySelected(priority: TaskPriority) {
        uiState = uiState.copy(priority = priority)
    }

    fun onCategorySelected(category: TaskCategory) {
        uiState = uiState.copy(category = category)
    }

    fun saveTask(context: Context) {
        // Validar solo el título (obligatorio)
        if (uiState.taskName.isBlank()) {
            uiState = uiState.copy(taskNameError = "El título es obligatorio")
            return
        }
        
        viewModelScope.launch {
            val sessionManager = SessionManager.getInstance(context)
            val serverUserId = sessionManager.getServerUserId()

            if (serverUserId == null) {
                uiState = uiState.copy(saveError = "Usuario no registrado en el servidor. Por favor, registrate primero.")
                return@launch
            }

            // Crear TaskRequest simple con solo los campos necesarios
            val taskRequest = TaskRequest(
                title = uiState.taskName.trim(),
                description = if (uiState.description.isNotBlank()) uiState.description.trim() else null,
                userId = serverUserId,
                priority = uiState.priority.apiValue,
                category = uiState.category.apiValue
            )

            try {
                val response = apiService.createTask(taskRequest)
                if (response.isSuccessful) {
                    uiState = uiState.copy(isTaskSaved = true, saveError = null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when (response.code()) {
                        400 -> "Error: Datos inválidos. Verifica los campos."
                        401 -> "Error: No autorizado. Gateway rechazó la petición. Verifica autenticación."
                        403 -> "Error: Acceso denegado"
                        404 -> "Error: Servicio no encontrado. Verifica que el Gateway esté enrutando a /api/tasks"
                        500 -> "Error: Problema en el servidor"
                        502 -> "Error: Gateway no puede conectarse al microservicio de tareas"
                        503 -> "Error: Servicio no disponible"
                        else -> "Error al guardar: código ${response.code()}"
                    }
                    android.util.Log.e("AddTaskViewModel", "Error response: $errorBody")
                    uiState = uiState.copy(saveError = "$errorMessage\nCódigo: ${response.code()}")
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Error: No se puede conectar al servidor. Verifica que esté ejecutándose en http://localhost:8071"
                    e.message?.contains("timeout") == true -> 
                        "Error: Tiempo de espera agotado"
                    else -> "Error de red: ${e.message}"
                }
                uiState = uiState.copy(saveError = errorMessage)
            }
        }
    }
}

data class AddTaskUiState(
    val taskName: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: TaskCategory = TaskCategory.WORK,
    val taskNameError: String? = null,
    val descriptionError: String? = null,
    val isTaskSaved: Boolean = false,
    val saveError: String? = null
) {
    val canSave: Boolean get() = taskName.isNotBlank()
}

enum class TaskPriority(val displayName: String, val apiValue: String) {
    LOW("Baja", "LOW"),
    MEDIUM("Media", "MEDIUM"),
    HIGH("Alta", "HIGH");
    
    companion object {
        fun fromApiValue(value: String): TaskPriority {
            return entries.find { it.apiValue == value } ?: MEDIUM
        }
    }
}

enum class TaskCategory(val displayName: String, val apiValue: String) {
    WORK("Trabajo", "WORK"),
    PERSONAL("Personal", "PERSONAL"),
    STUDY("Estudio", "STUDY"),
    HEALTH("Salud", "HEALTH"),
    HOME("Hogar", "HOME"),
    OTHER("Otro", "OTHER")
}
