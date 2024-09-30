package com.example.todo_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_app.ui.theme.TodoappTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo_app.presentation.settings.SettingsScreen
import com.example.todo_app.presentation.tasklist.TaskListScreen
import com.example.todo_app.presentation.taskcreate.TaskCreateScreen
import com.example.todo_app.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoappTheme {
                TodoNavHost()
            }
        }
    }
}

@Composable
fun TodoNavHost() {
    val navController: NavHostController = rememberNavController()
    val taskViewModel: TaskViewModel = viewModel()

    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            TaskListScreen(
                taskViewModel = taskViewModel,
                onAddTaskClick = { navController.navigate("create_task") },
                onTaskClick = { task -> navController.navigate("edit_task/${task.id}") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable("create_task") {
            TaskCreateScreen(
                taskViewModel = taskViewModel,
                onTaskSaved = { navController.popBackStack() }
            )
        }
        composable("edit_task/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            TaskCreateScreen(
                taskViewModel = taskViewModel,
                existingTaskId = taskId,
                onTaskSaved = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsScreen(
                taskViewModel = taskViewModel,
                onSaveSettings = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoappTheme {
        TodoNavHost()
    }
}