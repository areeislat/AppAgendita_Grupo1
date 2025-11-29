package com.example.appagendita_grupo1.data.repository

import android.util.Log
import com.example.appagendita_grupo1.data.local.task.TaskDao
import com.example.appagendita_grupo1.data.local.task.TaskEntity
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    @Named("taskApi") private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    fun getAllTasks(): Flow<List<TaskEntity>> = flow {
        val userId = sessionManager.getCurrentUserId()
            ?: throw IllegalStateException("Usuario no logueado")

        // Emitir datos locales inmediatamente
        val localTasks = taskDao.getTasksByUserId(userId).first()
        emit(localTasks)

        // TODO: Implementar obtención de tareas desde la API cuando esté disponible
        // Por ahora, las tareas solo se envían a la API pero no se recuperan
    }

    suspend fun addTask(
        title: String, 
        description: String?, 
        priority: String = "MEDIUM", 
        category: String = "WORK",
        startDate: LocalDate? = null,
        startTime: LocalTime? = null,
        endTime: LocalTime? = null
    ) {
        val userId = sessionManager.getCurrentUserId()
        if (userId == null) {
            Log.e("TaskRepository", "Usuario no logueado - creando sesión temporal para testing")
            // Temporal fix: crear sesión de prueba si no existe
            sessionManager.saveSession(
                userId = "test-user-id-123", 
                userEmail = "test@example.com", 
                userName = "Test User"
            )
        }
        val finalUserId = sessionManager.getCurrentUserId()
        if (finalUserId == null) {
            throw IllegalStateException("No se pudo crear sesión de usuario")
        }
        Log.d("TaskRepository", "Guardando tarea para usuario: $finalUserId")

        // Crear entidad temporal para guardar localmente si falla la red
        val tempTask = TaskEntity(
            title = title,
            description = description,
            userId = finalUserId,
            priority = priority,
            category = category,
            startDate = startDate,
            startTime = startTime,
            endTime = endTime
        )
        Log.d("TaskRepository", "Entidad creada: $tempTask")

        try {
            // Intentar enviar a la API
            val request = TaskRequest(
                title = title,
                description = description,
                userId = finalUserId,
                priority = priority,
                category = category
            )

            val response = apiService.createTask(request)

            if (response.isSuccessful) {
                Log.d("TaskRepository", "Tarea subida exitosamente a la nube")
                // También guardar localmente para que el usuario la vea inmediatamente
                taskDao.insert(tempTask)
            } else {
                Log.w("TaskRepository", "Error en servidor, guardando localmente: ${response.code()}")
                taskDao.insert(tempTask)
            }

        } catch (e: Exception) {
            Log.w("TaskRepository", "Sin conexión, guardando tarea localmente: ${e.message}")
            Log.e("TaskRepository", "Stack trace:", e)
            try {
                taskDao.insert(tempTask)
                Log.d("TaskRepository", "Tarea guardada localmente exitosamente")
            } catch (dbException: Exception) {
                Log.e("TaskRepository", "Error al guardar en base de datos local: ${dbException.message}")
                Log.e("TaskRepository", "DB Stack trace:", dbException)
                throw dbException
            }
        }
    }

    suspend fun updateTaskCompletion(taskId: Long, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }

    suspend fun getPendingTasksCount(): Int {
        val userId = sessionManager.getCurrentUserId() ?: return 0
        return taskDao.getPendingTasksCount(userId)
    }
}