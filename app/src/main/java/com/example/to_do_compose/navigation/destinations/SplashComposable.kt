package com.example.to_do_compose.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.to_do_compose.ui.screens.splash.SplashScreen
import com.example.to_do_compose.utils.Constants

fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit
){
    composable(
        route = Constants.SPLASH_SCREEN
    ){
        SplashScreen(
            navigateToListScreen = navigateToListScreen
        )
    }
}