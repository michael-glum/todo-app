package com.example.todo_app.data.repository

import com.example.todo_app.data.model.Task
import com.example.todo_app.data.model.TaskRequest
import com.example.todo_app.data.remote.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class TaskRepository {

    private val api = RetrofitClient.taskApiService

    suspend fun fetchTasks(completed: Boolean? = null, sortBy: String? = null): Result<List<Task>> {
        return try {
            val response = api.getTasks(completed, sortBy)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error fetching tasks: ${response.message()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.message()}"))
        }
    }

    suspend fun createTask(task: TaskRequest): Result<Task> {
        return try {
            val response = api.createTask(task)
            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("Empty task response"))
            } else {
                Result.failure(Exception("Error creating task: ${response.message()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.message()}"))
        }
    }

    suspend fun updateTask(id: String, task: TaskRequest): Result<Task> {
        return try {
            val response = api.updateTask(id, task)
            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("Empty task response"))
            } else {
                Result.failure(Exception("Error updating task: ${response.message()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.message()}"))
        }
    }

    suspend fun deleteTask(id: String): Result<Boolean> {
        return try {
            val response = api.deleteTask(id)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error deleting task: ${response.message()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.message()}"))
        }
    }
}