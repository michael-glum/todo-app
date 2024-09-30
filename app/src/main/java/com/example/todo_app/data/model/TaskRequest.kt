package com.example.todo_app.data.model

import java.util.Date

data class TaskRequest(
    val taskDescription: String,
    val dueDate: Date,
    val completed: Boolean
)