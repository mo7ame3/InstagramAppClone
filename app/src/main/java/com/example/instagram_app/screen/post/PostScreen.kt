package com.example.instagram_app.screen.post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagram_app.R
import com.example.instagram_app.components.BottomNavigationItem
import com.example.instagram_app.components.BottomNavigationMenu
import com.example.instagram_app.components.CommonProgressSpinner
import com.example.instagram_app.components.UserImageCard
import com.example.instagram_app.navigation.AllScreens
import com.example.instagram_app.screen.InstagramViewModel

@Composable
fun PostScreen(navController: NavController, viewModel: InstagramViewModel) {
    val userData = viewModel.userData.value
    val isLoading = viewModel.inProgress.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row {
                ProfileImage(imageUrl = userData?.imageUrl) {

                }
                Text(
                    text = "15\nposts",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "54\nfollowers",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "54\nfollowing",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                val userNameDisplay =
                    if (userData?.userName == null) "" else "@${userData.userName}"
                Text(text = userData?.name ?: "", fontWeight = FontWeight.Bold)
                Text(text = userNameDisplay)
                Text(text = userData?.bio ?: "")
            }
            OutlinedButton(
                onClick = {
                    navController.navigate(route = AllScreens.ProfileScreen.name)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                ), shape = RoundedCornerShape(10)
            ) {
                Text(text = "Edit Profile", color = Color.Black)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Post List")
            }
        }
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.POST,
            navController = navController
        )
    }

    if (isLoading) {
        CommonProgressSpinner()
    }

}

@Composable
fun ProfileImage(imageUrl: String?, onClick: () -> Unit) {
    Box(modifier = Modifier
        .padding(top = 16.dp)
        .clickable { onClick.invoke() }) {
        UserImageCard(
            userImage = imageUrl, modifier = Modifier
                .padding(8.dp)
                .size(88.dp)
        )

        Card(
            shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.White),
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = null,
                modifier = Modifier.background(
                    Color.Blue
                )
            )
        }

    }
}