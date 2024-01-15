package com.example.instagram_app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagram_app.R
import com.example.instagram_app.navigation.AllScreens

enum class BottomNavigationItem(val icon: Int, val route: String) {
    FEED(R.drawable.home, AllScreens.FeedScreen.name),
    SEARCH(R.drawable.search, AllScreens.SearchScreen.name),
    POST(R.drawable.person, AllScreens.PostScreen.name)
}

@Composable
fun BottomNavigationMenu(selectedItem: BottomNavigationItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color.White)
    ) {
        for (item in BottomNavigationItem.values()) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(5.dp)
                    .weight(1f)
                    .clickable {
                        navController.navigate(route = item.route)
                    },
                colorFilter = if (item == selectedItem) ColorFilter.tint(Color.Black) else ColorFilter.tint(
                    Color.Gray
                )
            )
        }
    }
}