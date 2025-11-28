package com.example.appagendita_grupo1.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String = "",
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("profileImageUrl") val profileImageUrl: String? = null,
    @SerializedName("role") val role: String = "USER",
    @SerializedName("active") val active: Boolean = true,
    @SerializedName("emailVerified") val emailVerified: Boolean = false,
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = ""
)
