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
import com.example.instagram_app.screen.feed.FeedScreen
import com.example.instagram_app.screen.post.PostScreen
import com.example.instagram_app.screen.profile.ProfileScreen
import com.example.instagram_app.screen.search.SearchScreen
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
        composable(route = AllScreens.FeedScreen.name) {
            FeedScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AllScreens.SearchScreen.name) {
            SearchScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AllScreens.PostScreen.name) {
            PostScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AllScreens.ProfileScreen.name) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
    }
}