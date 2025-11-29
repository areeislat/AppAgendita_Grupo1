package com.example.appagendita_grupo1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.di.TaskApi
import com.example.appagendita_grupo1.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


import javax.inject.Named
@HiltViewModel

class AddTaskViewModel @Inject constructor(
    @Named("taskApi") private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var uiState by mutableStateOf(AddTaskUiState())
        private set

    private var nextSubtaskId = 0L

    init {
        val now = LocalDateTime.now()
        val normalizedStart = now.toLocalTime().withSecond(0).withNano(0)
        val defaultEnd = normalizedStart.plusHours(1)
        uiState = uiState.copy(
            startDate = now.toLocalDate(),
            startTime = normalizedStart,
            endTime = defaultEnd,
            selectedColor = TaskColorOption.pastelPalette.first()
        )
    }

    fun onTaskNameChange(value: String) {
        val error = if (value.isBlank()) "El nombre de la tarea no puede estar vacío" else null
        uiState = uiState.copy(taskName = value, taskNameError = error)
    }

    fun onDescriptionChange(value: String) {
        uiState = uiState.copy(description = value)
    }

    fun onPrioritySelected(priority: TaskPriority) {
        uiState = uiState.copy(priority = priority)
    }

    fun onCategorySelected(category: TaskCategory) {
        uiState = uiState.copy(category = category)
    }

    fun onDateSelected(date: LocalDate) {
        uiState = uiState.copy(startDate = date)
    }

    fun onStartTimeSelected(time: LocalTime) {
        var newEndTime = uiState.endTime
        if (!time.isBefore(newEndTime)) {
            newEndTime = time.plusMinutes(30)
        }
        uiState = uiState.copy(startTime = time, endTime = newEndTime, timeError = null)
    }

    fun onEndTimeSelected(time: LocalTime) {
        if (time.isBefore(uiState.startTime)) {
            uiState = uiState.copy(timeError = "La hora de término no puede ser anterior a la de inicio")
        } else {
            uiState = uiState.copy(endTime = time, timeError = null)
        }
    }

    fun onColorSelected(option: TaskColorOption) {
        uiState = uiState.copy(selectedColor = option)
    }

    fun onReminderSelected(option: ReminderOption) {
        uiState = uiState.copy(reminder = option)
    }

    fun onSubtasksToggle(enabled: Boolean) {
        if (enabled) {
            val updatedSubtasks = if (uiState.subtasks.isEmpty()) listOf(createSubtask()) else uiState.subtasks
            uiState = uiState.copy(subtasksEnabled = true, subtasks = updatedSubtasks)
        } else {
            uiState = uiState.copy(subtasksEnabled = false, subtasks = emptyList())
        }
    }

    fun onAddSubtask() {
        if (!uiState.subtasksEnabled) return
        uiState = uiState.copy(subtasks = uiState.subtasks + createSubtask())
    }

    fun onSubtaskTitleChange(id: Long, title: String) {
        uiState = uiState.copy(
            subtasks = uiState.subtasks.map { subtask ->
                if (subtask.id == id) subtask.copy(title = title) else subtask
            }
        )
    }

    fun onSubtaskCheckedChange(id: Long, checked: Boolean) {
        uiState = uiState.copy(
            subtasks = uiState.subtasks.map { subtask ->
                if (subtask.id == id) subtask.copy(isDone = checked) else subtask
            }
        )
    }

    fun onRemoveSubtask(id: Long) {
        uiState = uiState.copy(subtasks = uiState.subtasks.filterNot { it.id == id })
    }

    private fun createSubtask(): SubtaskUiState {
        return SubtaskUiState(id = nextSubtaskId++)
    }

    fun saveTask() {
        if (!uiState.canSave) return

        viewModelScope.launch {
            try {
                val userId = sessionManager.getCurrentUserId() ?: return@launch

                val request = TaskRequest(
                    title = uiState.taskName,
                    description = uiState.description,
                    userId = userId,
                    // Enviamos el nombre del enum (ej: "WORK", "HIGH") que es lo que espera Spring Boot
                    priority = uiState.priority.name,
                    category = uiState.category.name
                )

                val response = apiService.createTask(request)
                if (response.isSuccessful) {
                    uiState = uiState.copy(isTaskSaved = true)
                } else {
                    uiState = uiState.copy(saveError = "Error al guardar: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiState = uiState.copy(saveError = "Error de conexión: ${e.message}")
            }
        }
    }
}

data class AddTaskUiState(
    val taskName: String = "",
    val description: String = "",
    // Valores por defecto alineados con el backend
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: TaskCategory = TaskCategory.WORK,
    val isTaskSaved: Boolean = false,
    val saveError: String? = null,
    val startDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now().withSecond(0).withNano(0),
    val endTime: LocalTime = LocalTime.now().withSecond(0).withNano(0).plusHours(1),
    val taskType: TaskType = TaskType.IN_PROGRESS,
    val selectedColor: TaskColorOption = TaskColorOption.pastelPalette.first(),
    val subtasksEnabled: Boolean = false,
    val subtasks: List<SubtaskUiState> = emptyList(),
    val reminder: ReminderOption = ReminderOption.NONE,
    val taskNameError: String? = null,
    val timeError: String? = null
) {
    val canSave: Boolean get() = taskName.isNotBlank() && timeError == null
}

data class SubtaskUiState(
    val id: Long,
    val title: String = "",
    val isDone: Boolean = false
)

// --- ENUMS ACTUALIZADOS (COINCIDENCIA EXACTA CON BACKEND) ---

enum class TaskPriority(val displayName: String) {
    LOW("Baja"),
    MEDIUM("Media"),
    HIGH("Alta"),
    URGENT("Urgente")
}

enum class TaskCategory(val displayName: String) {
    WORK("Trabajo"),
    PERSONAL("Personal"),
    FAMILY("Familia"),
    HEALTH("Salud"),
    EDUCATION("Educación"),
    SHOPPING("Compras"),
    TRAVEL("Viajes"),      // Añadido para coincidir con Backend
    FINANCE("Finanzas"),
    HOME("Hogar"),         // Añadido para coincidir con Backend
    OTHER("Otro")
}
// ------------------------------------------------------------

enum class TaskType(val displayName: String) {
    URGENT("Urgente"),
    IN_PROGRESS("En proceso"),
    PENDING("Pendiente");
}

enum class ReminderOption(val label: String) {
    NONE("Sin recordatorio"),
    FIFTEEN_MINUTES("15 minutos antes"),
    ONE_HOUR("1 hora antes"),
    ONE_DAY("El día anterior"),
    CUSTOM("Personalizado");
}

data class TaskColorOption(val label: String, val color: Color) {
    companion object {
        val pastelPalette: List<TaskColorOption> = listOf(
            TaskColorOption("Lavanda", Color(0xFFD7C2F9)),
            TaskColorOption("Melocotón", Color(0xFFFFD8C2)),
            TaskColorOption("Aqua", Color(0xFFC2F2F0)),
            TaskColorOption("Menta", Color(0xFFC9F7D5)),
            TaskColorOption("Sol", Color(0xFFFFF0B3)),
            TaskColorOption("Cielo", Color(0xFFC7E5FF))
        )
    }
}