package com.example.todo_app.presentation.taskcreate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.R
import com.example.todo_app.data.model.TaskRequest
import com.example.todo_app.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateScreen(
    taskViewModel: TaskViewModel,
    onTaskSaved: () -> Unit,
    existingTaskId: String? = null
) {
    val tasks by taskViewModel.tasks.collectAsState()

    var taskDescription by remember { mutableStateOf("") }
    var taskDueDate by remember { mutableStateOf(Date()) }
    var taskDueDateString by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Prepopulate fields if editing
    LaunchedEffect(existingTaskId) {
        existingTaskId?.let {
            tasks.find { it.id == existingTaskId }?.let { task ->
                taskDescription = task.taskDescription
                taskDueDate = task.dueDate
                isCompleted = task.completed
            }
        }
    }

    LaunchedEffect(taskDueDate) {
        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        taskDueDateString = formatter.format(taskDueDate)
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        time = taskDueDate
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            R.style.CustomDatePickerDialog,
            { _, selectedYear, selectedMonth, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.time

            taskDueDate = selectedDate
        }, year, month, day)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (existingTaskId != null) "Edit" else "Create",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 30.sp
                    )
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("To-Do Item Name", modifier = Modifier.padding(2.dp))
            TextField(
                value = taskDescription,
                onValueChange = {
                    taskDescription = it
                    descriptionError = taskDescription.isEmpty()
                },
                label = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD9D9D9))
                    .border(
                        if (taskDescription.isNotEmpty()) BorderStroke(1.dp, Color.Black)
                        else BorderStroke(0.dp, Color.Transparent)
                    ),
                isError = descriptionError,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
            )
            if (descriptionError) {
                Text(
                    text = "Task description cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text("Select Due Date", modifier = Modifier.padding(start = 2.dp))

            OutlinedTextField(
                value = taskDueDateString,
                onValueChange = {},
                readOnly = true,
                label = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
                    .background(Color(0xFFD9D9D9))
                    .clickable { datePickerDialog.show() },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_today),
                            contentDescription = "Select Due Date",
                            tint = Color.Black,
                        )
                    }
                },
                shape = RoundedCornerShape(4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (taskDescription.isEmpty()) {
                        descriptionError = true
                        return@Button
                    }

                    isSaving = true

                    if (existingTaskId != null) {
                        taskViewModel.updateTask(
                            existingTaskId,
                            TaskRequest(taskDescription, taskDueDate, isCompleted)
                        )
                    } else {
                        taskViewModel.createTask(TaskRequest(taskDescription, taskDueDate, isCompleted))
                    }
                    onTaskSaved()
                },
                modifier = Modifier
                    .width(90.dp)
                    .height(40.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(
                        text = "Save",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}