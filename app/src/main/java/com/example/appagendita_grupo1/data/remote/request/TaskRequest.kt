package com.example.appagendita_grupo1.data.remote.request

import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("userId") val userId: String,
    @SerializedName("priority") val priority: String = "MEDIUM",
    @SerializedName("category") val category: String = "WORK"
)
