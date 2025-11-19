package com.example.appagendita_grupo1.data.remote

import com.example.appagendita_grupo1.data.remote.request.RegisterRequest
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/tasks")
    suspend fun createTask(@Body taskRequest: TaskRequest): Response<Unit>

    @POST("api/users")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<UserResponse>

}
