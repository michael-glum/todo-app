package com.example.todo_app.data.remote

import com.example.todo_app.data.model.Task
import com.example.todo_app.data.model.TaskRequest
import retrofit2.Response
import retrofit2.http.*

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(
        @Query("completed") completed: Boolean? = null,
        @Query("sort_by") sortBy: String? = null
    ): Response<List<Task>>

    @GET("tasks/{id}")
    suspend fun getTask(@Path("id") id: String): Response<Task>

    @POST("tasks")
    suspend fun createTask(@Body task: TaskRequest): Response<Task>

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: TaskRequest): Response<Task>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Void>
}