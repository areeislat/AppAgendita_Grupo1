package com.example.appagendita_grupo1.data.remote.request

import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("category") val category: String,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("reminderDate") val reminderDate: String,
    @SerializedName("estimatedDurationMinutes") val estimatedDurationMinutes: Int,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("location") val location: String,
    @SerializedName("isRecurring") val isRecurring: Boolean,
    @SerializedName("recurrenceType") val recurrenceType: String,
    @SerializedName("recurrenceEndDate") val recurrenceEndDate: String,
    @SerializedName("parentTaskId") val parentTaskId: String?,
    @SerializedName("orderIndex") val orderIndex: Int
)
