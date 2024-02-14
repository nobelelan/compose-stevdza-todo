package com.example.to_do_compose.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.to_do_compose.ui.screens.list.ListScreen
import com.example.to_do_compose.ui.viewmodels.SharedViewModel
import com.example.to_do_compose.utils.Action
import com.example.to_do_compose.utils.Action.NO_ACTION
import com.example.to_do_compose.utils.Constants.LIST_ARGUMENT_KEY
import com.example.to_do_compose.utils.Constants.LIST_SCREEN
import com.example.to_do_compose.utils.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
){
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){ navBackStackEntry ->

        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        var myAction by rememberSaveable { mutableStateOf(NO_ACTION) }

        LaunchedEffect(key1 = action){
            if( action != myAction){
                myAction = action
                sharedViewModel.action.value = action
            }
        }

        val databaseAction by sharedViewModel.action

        ListScreen(
            action = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}