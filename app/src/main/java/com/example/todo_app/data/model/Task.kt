package com.example.todo_app.data.model

import java.util.Date

data class Task(
    val id: String,
    val taskDescription: String,
    val createdDate: Date,
    val dueDate: Date,
    val completed: Boolean
)
