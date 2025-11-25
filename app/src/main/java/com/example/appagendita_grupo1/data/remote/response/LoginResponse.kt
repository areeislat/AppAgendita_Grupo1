package com.example.appagendita_grupo1.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserResponse
)