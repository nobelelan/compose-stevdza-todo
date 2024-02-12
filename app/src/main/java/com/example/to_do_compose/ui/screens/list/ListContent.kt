package com.example.to_do_compose.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.to_do_compose.data.models.Priority
import com.example.to_do_compose.data.models.ToDoTask
import com.example.to_do_compose.ui.theme.LARGE_PADDING
import com.example.to_do_compose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.to_do_compose.ui.theme.TASK_ITEM_ELEVATION
import com.example.to_do_compose.ui.theme.taskItemBackgroundColor
import com.example.to_do_compose.ui.theme.taskItemTextColor
import com.example.to_do_compose.utils.RequestState
import com.example.to_do_compose.utils.SearchAppBarState

@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortState: RequestState<Priority>,
    searchedTasks: RequestState<List<ToDoTask>>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    searchAppBarState: SearchAppBarState,
    contentPadding: PaddingValues
) {
    if(sortState is RequestState.Success){
        when{
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success){
                    HandleListScreen(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        contentPadding = contentPadding
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if (allTasks is RequestState.Success){
                    HandleListScreen(
                        tasks = allTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        contentPadding = contentPadding
                    )
                }
            }
            sortState.data == Priority.LOW -> {
                HandleListScreen(
                    tasks = lowPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    contentPadding = contentPadding
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListScreen(
                    tasks = highPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    contentPadding = contentPadding
                )
            }
        }
    }
}

@Composable
fun HandleListScreen(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    contentPadding: PaddingValues
) {
    if (tasks.isEmpty()){
        EmptyContent()
    }else{
        DisplayTasks(
            tasks = tasks,
            navigateToTaskScreen = navigateToTaskScreen,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .padding(contentPadding)
    ){
        items(
            items = tasks,
            key = { task ->
                task.id
            }
        ){ task ->
            TaskItem(
                toDoTask = task,
                navigateToTaskScreen = navigateToTaskScreen
            )
        }
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.taskItemBackgroundColor,
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(8f),
                    text = toDoTask.title,
                    color = MaterialTheme.colorScheme.taskItemTextColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ){
                    Canvas(
                        modifier = Modifier
                            .size(PRIORITY_INDICATOR_SIZE) // size replaces width and height
                    ){
                        drawCircle(
                            color = toDoTask.priority.color
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                color = MaterialTheme.colorScheme.taskItemTextColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

//@Composable
//@Preview
//fun TaskItemPreview() {
//    TaskItem(
//        toDoTask = ToDoTask(0, "Hahaha", "Hoho ho nothing to as", Priority.HIGH),
//        navigateToTaskScreen = {}
//    )
//}