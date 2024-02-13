package com.example.to_do_compose.navigation

import androidx.navigation.NavHostController
import com.example.to_do_compose.utils.Action
import com.example.to_do_compose.utils.Constants.LIST_SCREEN
import com.example.to_do_compose.utils.Constants.SPLASH_SCREEN

class Screens(navController: NavHostController) {
    val splash: () -> Unit = {
        navController.navigate(route = "list/${Action.NO_ACTION.name}"){
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }
    val list: (Int) -> Unit = { taskId->
        navController.navigate(route = "task/${taskId}")
    }
    val task: (Action) -> Unit = { action ->
        navController.navigate(route = "list/${action.name}"){
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
}