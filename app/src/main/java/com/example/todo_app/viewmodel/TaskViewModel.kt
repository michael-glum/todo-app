package com.example.todo_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.model.Task
import com.example.todo_app.data.model.TaskRequest
import com.example.todo_app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _filterOption = MutableStateFlow("All")
    val filterOption: StateFlow<String> = _filterOption

    private val _sortByOption = MutableStateFlow("Due")
    val sortByOption: StateFlow<String> = _sortByOption

    private val _sortOrder = MutableStateFlow("Ascending")
    val sortOrder: StateFlow<String> = _sortOrder

    fun applySettings(filter: String, sortBy: String, order: String) {
        _filterOption.value = filter
        _sortByOption.value = sortBy
        _sortOrder.value = order
    }

    // Fetch tasks from repository and apply filter/sort
    fun fetchTasks() {
        viewModelScope.launch {
            val completed: Boolean? = when (_filterOption.value) {
                "Complete" -> true
                "Incomplete" -> false
                else -> null
            }

            val sortBy: String? = when (_sortByOption.value) {
                "Created" -> if (_sortOrder.value == "Ascending") "+createdDate" else "-createdDate"
                "Due" -> if (_sortOrder.value == "Ascending") "+dueDate" else "-dueDate"
                else -> null
            }

            val result = repository.fetchTasks(completed, sortBy)
            result.fold(
                onSuccess = { fetchedTasks ->
                    _tasks.value = fetchedTasks
                    _error.value = null
                },
                onFailure = { error ->
                    _error.value = error.localizedMessage
                }
            )
        }
    }

    // Create a new task in the repository and update the list
    fun createTask(taskRequest: TaskRequest) {
        viewModelScope.launch {
            val result = repository.createTask(taskRequest)
            result.fold(
                onSuccess = { newTask ->
                    _tasks.value += newTask
                    // Re-fetch tasks to ensure consistency with the server
                    fetchTasks()
                    _error.value = null
                },
                onFailure = { error ->
                    _error.value = error.localizedMessage
                }
            )
        }
    }

    // Update a task and apply changes to the list
    fun updateTask(id: String, taskRequest: TaskRequest) {
        viewModelScope.launch {
            val result = repository.updateTask(id, taskRequest)
            result.fold(
                onSuccess = { updatedTask ->
                    _tasks.value = _tasks.value.map { task -> if (task.id == id) updatedTask else task }
                    _error.value = null
                },
                onFailure = { error ->
                    _error.value = error.localizedMessage
                }
            )
        }
    }

    // Delete a task by ID and remove it from the list
    fun deleteTask(id: String) {
        viewModelScope.launch {
            val result = repository.deleteTask(id)
            result.fold(
                onSuccess = { success ->
                    if (success) {
                        _tasks.value = _tasks.value.filter { it.id != id }
                        _error.value = null
                    }
                },
                onFailure = { error ->
                    _error.value = error.localizedMessage
                }
            )
        }
    }

    // Toggle task completed status
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val taskRequest = TaskRequest(
                taskDescription = task.taskDescription,
                dueDate = task.dueDate,
                completed = !task.completed
            )

            val result = repository.updateTask(task.id, taskRequest)
            result.fold(
                onSuccess = {
                    _tasks.value = _tasks.value.map { if (it.id == task.id) it.copy(completed = !it.completed) else it }
                    _error.value = null
                },
                onFailure = { error ->
                    _error.value = error.localizedMessage
                }
            )
        }
    }
}