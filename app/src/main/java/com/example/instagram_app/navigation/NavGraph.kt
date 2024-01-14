package com.example.instagram_app.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagram_app.components.NotificationMessage
import com.example.instagram_app.screen.InstagramViewModel
import com.example.instagram_app.screen.auth.LoginScreen
import com.example.instagram_app.screen.auth.SingUpScreen
import com.example.instagram_app.screen.home.HomeScreen
import com.example.instagram_app.screen.splash.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<InstagramViewModel>()
    NotificationMessage(viewModel = viewModel)
    NavHost(navController = navController, startDestination = AllScreens.SplashScreen.name) {
        composable(route = AllScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = AllScreens.SingUpScreen.name) {
            SingUpScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AllScreens.LoginScreen.name) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AllScreens.HomeScreen.name) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
    }
}