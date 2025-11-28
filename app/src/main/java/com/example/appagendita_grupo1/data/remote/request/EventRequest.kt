package com.example.appagendita_grupo1.data.remote.request

import com.google.gson.annotations.SerializedName

data class EventRequest(
    @SerializedName("owner_id") val ownerId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("event_timestamp") val eventTimestamp: String,
    @SerializedName("location") val location: String?
)
