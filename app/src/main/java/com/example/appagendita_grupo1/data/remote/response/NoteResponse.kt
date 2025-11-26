package com.example.appagendita_grupo1.data.remote.response

import com.google.gson.annotations.SerializedName

data class NoteResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageUri") val imageUri: String? = null,
    @SerializedName("userId") val userId: String,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)