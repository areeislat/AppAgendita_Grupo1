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
import javax.inject.Inject
import javax.inject.Named

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    @Named("taskApi") private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    /**
     * Obtiene tareas combinando API y Base de Datos Local.
     * Estrategia: Emitir local -> Intentar API -> Emitir combinado (Local + API)
     * NOTA: Por ahora solo emite datos locales ya que la API no tiene endpoint para obtener tareas
     */
    fun getAllTasks(): Flow<List<TaskEntity>> = flow {
        // 1. Obtener UUID del usuario (String) - crear sesión temporal si no existe
        val userId = sessionManager.getCurrentUserId() ?: run {
            Log.w("TaskRepository", "No hay usuario logueado para obtener tareas, creando sesión temporal")
            sessionManager.saveSession(
                userId = "temp-user-${System.currentTimeMillis()}", 
                userEmail = "temp@test.com", 
                userName = "Usuario Temporal"
            )
            sessionManager.getCurrentUserId() ?: throw IllegalStateException("No se pudo crear sesión temporal")
        }

        // 2. Emitir datos locales inmediatamente (Para que la UI cargue rápido)
        val localTasks = taskDao.getTasksByUserId(userId).first()
        emit(localTasks)

        // TODO: Implementar obtención de tareas desde la API cuando esté disponible
        // Por ahora, las tareas solo se envían a la API pero no se recuperan
        try {
            Log.d("TaskRepository", "API de tareas aún no implementa GET, solo mostrando datos locales")
        } catch (e: Exception) {
            Log.e("TaskRepository", "Error de red al obtener tareas: ${e.message}")
        }
    }

    /**
     * Lógica Store-and-Forward IGUAL A NOTAS:
     * Intenta subir a la API. Si falla (offline), guarda en local.
     */
    suspend fun addTask(
        title: String, 
        description: String?, 
        priority: String = "MEDIUM", 
        category: String = "WORK"
    ) {
        // Igual que notas: si no hay usuario, salir sin crash
        val userId = sessionManager.getCurrentUserId() ?: run {
            // Crear sesión temporal para testing si no existe
            Log.w("TaskRepository", "No hay usuario logueado, creando sesión temporal")
            sessionManager.saveSession(
                userId = "temp-user-${System.currentTimeMillis()}", 
                userEmail = "temp@test.com", 
                userName = "Usuario Temporal"
            )
            sessionManager.getCurrentUserId() ?: return
        }

        // Creamos la entidad temporal (para guardar en local si falla la red)
        val tempTask = TaskEntity(
            title = title,
            description = description,
            userId = userId,
            priority = priority,
            category = category
        )

        try {
            // 1. Crear Request para la API
            val request = TaskRequest(
                title = title,
                description = description,
                userId = userId,
                priority = priority,
                category = category
            )

            // 2. Intentar enviar a la API
            val response = apiService.createTask(request)

            if (response.isSuccessful) {
                // Éxito: La tarea ya está en la nube.
                // No la guardamos en local para no duplicar (se descargará al refrescar).
                Log.d("TaskRepository", "Tarea subida exitosamente a la nube")
            } else {
                // Fallo de servidor (ej. 500): Guardar localmente para reintentar luego
                Log.w("TaskRepository", "Error en servidor, guardando localmente")
                taskDao.insert(tempTask)
            }

        } catch (e: Exception) {
            // 3. Fallo de Red (Offline): Guardar localmente
            Log.w("TaskRepository", "Sin conexión, guardando tarea localmente: ${e.message}")
            taskDao.insert(tempTask)
        }
    }

    /**
     * Actualiza el estado de completado de una tarea.
     * Por ahora solo localmente.
     */
    suspend fun updateTaskCompletion(taskId: Long, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted)
    }

    /**
     * Elimina una tarea.
     * Por ahora solo eliminamos localmente.
     * TODO: Implementar borrado en API si la tarea tiene un ID remoto.
     */
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }

    /**
     * Cuenta las tareas pendientes del usuario
     */
    suspend fun getPendingTasksCount(): Int {
        val userId = sessionManager.getCurrentUserId() ?: return 0
        return taskDao.getPendingTasksCount(userId)
    }
}