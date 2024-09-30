package com.example.todo_app.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    taskViewModel: TaskViewModel,
    onSaveSettings: () -> Unit
) {
    var filterOption by remember { mutableStateOf(taskViewModel.filterOption.value) }
    var sortByOption by remember { mutableStateOf(taskViewModel.sortByOption.value) }
    var sortOrder by remember { mutableStateOf(taskViewModel.sortOrder.value) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Filters",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = filterOption == "All",
                    onClick = { filterOption = "All" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text("All")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = filterOption == "Complete",
                    onClick = { filterOption = "Complete" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text("Complete")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = filterOption == "Incomplete",
                    onClick = { filterOption = "Incomplete" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text("Incomplete")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sort By",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = sortByOption == "Due",
                    onClick = { sortByOption = "Due" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text("Due Date")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = sortByOption == "Created",
                    onClick = { sortByOption = "Created" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text("Creation Date")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sort Date Direction",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = sortOrder == "Ascending",
                    onClick = { sortOrder = "Ascending" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text("Ascending")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = sortOrder == "Descending",
                    onClick = { sortOrder = "Descending" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    )
                )
                Text("Descending")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    taskViewModel.applySettings(
                        filter = filterOption,
                        sortBy = sortByOption,
                        order = sortOrder
                    )
                    onSaveSettings()
                },
                modifier = Modifier
                    .width(90.dp)
                    .height(40.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            ) {
                Text(
                    text = "Save",
                    fontSize = 18.sp
                )
            }
        }
    }
}