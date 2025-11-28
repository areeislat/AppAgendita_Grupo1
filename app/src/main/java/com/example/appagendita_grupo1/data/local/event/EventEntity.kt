package com.example.appagendita_grupo1.data.local.event

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String?,
    val eventTimestamp: LocalDateTime,
    val location: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)
