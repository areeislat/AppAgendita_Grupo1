package com.example.appagendita_grupo1.data.remote

import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/tasks")
    suspend fun createTask(@Body taskRequest: TaskRequest): Response<Unit> // Assuming the API returns an empty successful response

}
