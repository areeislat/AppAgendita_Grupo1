package com.example.appagendita_grupo1.data.local.task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    
    val title: String,
    val description: String?,
    val userId: String,
    val priority: String = "MEDIUM", // "LOW", "MEDIUM", "HIGH", "URGENT"
    val category: String = "WORK", // "WORK", "PERSONAL", etc.
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)