package com.example.appagendita_grupo1.data.remote.response

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("id") val id: String,
    @SerializedName("owner_id") val ownerId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("event_timestamp") val eventTimestamp: String,
    @SerializedName("location") val location: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?
)
