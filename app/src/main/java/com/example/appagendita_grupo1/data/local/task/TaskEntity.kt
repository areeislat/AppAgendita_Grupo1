package com.example.appagendita_grupo1.data.local.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    
    val title: String,
    val description: String?,
    val userId: String,
    val priority: String, // "LOW", "MEDIUM", "HIGH", "URGENT"
    val category: String, // "WORK", "PERSONAL", etc.
    val startDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)