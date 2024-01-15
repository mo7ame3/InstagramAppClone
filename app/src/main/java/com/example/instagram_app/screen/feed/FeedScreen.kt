package com.example.instagram_app.screen.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.instagram_app.components.BottomNavigationItem
import com.example.instagram_app.components.BottomNavigationMenu
import com.example.instagram_app.screen.InstagramViewModel

@Composable
fun FeedScreen(navController: NavController, viewModel: InstagramViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Feed")
        }
        BottomNavigationMenu(selectedItem = BottomNavigationItem.FEED, navController = navController)
    }
}