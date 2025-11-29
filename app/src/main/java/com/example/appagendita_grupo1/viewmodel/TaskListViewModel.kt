package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.local.task.TaskEntity
import com.example.appagendita_grupo1.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                taskRepository.getAllTasks().collect { taskList ->
                    _tasks.value = taskList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar tareas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            try {
                taskRepository.updateTaskCompletion(task.id, !task.isCompleted)
                loadTasks() // Refrescar la lista
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar tarea: ${e.message}"
            }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(task)
                loadTasks() // Refrescar la lista
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar tarea: ${e.message}"
            }
        }
    }
}