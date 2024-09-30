package com.example.todo_app.presentation.tasklist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.data.model.Task
import com.example.todo_app.viewmodel.TaskViewModel
import com.example.todo_app.R
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    taskViewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onSettingsClick: () -> Unit,

) {
    LaunchedEffect(Unit) {
        taskViewModel.fetchTasks()
    }

    val tasks by taskViewModel.tasks.collectAsState()
    val error by taskViewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Task List",
                            fontSize = 30.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onSettingsClick() },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Settings"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAddTaskClick() },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_create),
                            contentDescription = "Create Task",
                            tint = Color.Black,

                        )
                    }
                }
            )
        },
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (error != null) {
                Snackbar(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = error ?: "An unknown error occurred")
                }
            }

            if (tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tasks available")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onClick = onTaskClick,
                            onDeleteClick = { taskId -> taskViewModel.deleteTask(taskId) },
                            onSelectClick = { taskViewModel.toggleTaskCompletion(task) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onClick: (Task) -> Unit,
    onDeleteClick: (String) -> Unit,
    onSelectClick: (Task) -> Unit
) {
    val dateFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    val displayDueDate = dateFormatter.format(task.dueDate)
    val displayCreatedDate = dateFormatter.format(task.createdDate)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Edit Icon Button
            IconButton(onClick = { onClick(task) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Task Description, Due Date, and Created Date
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = task.taskDescription,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Due: $displayDueDate",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Created: $displayCreatedDate",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Select Icon Button
            IconButton(onClick = { onSelectClick(task) }) {
                Icon(
                    painter = painterResource(id = if (!task.completed) R.drawable.ic_checkbox else R.drawable.ic_checkbox_checked),
                    contentDescription = "Select Task",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Delete Icon Button
            IconButton(onClick = { onDeleteClick(task.id) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}