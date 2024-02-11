package com.example.to_do_compose.ui.screens.task

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.to_do_compose.data.models.Priority
import com.example.to_do_compose.data.models.ToDoTask
import com.example.to_do_compose.utils.Action

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    Scaffold(
        topBar = {
                 TaskAppBar(
                     navigateToListScreen = navigateToListScreen,
                     selectedTask = selectedTask
                 )
        },
        content = {
            TaskContent(
                title = "",
                onTitleChange = {},
                description = "",
                onDescriptionChange = {},
                priority = Priority.LOW,
                onPrioritySelected = {},
                contentPadding = it
            )
        }
    )
}