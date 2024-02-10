package com.example.instagram_app.screen.post

import PostData
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.instagram_app.R
import com.example.instagram_app.components.CommonDivider
import com.example.instagram_app.components.CommonImage
import com.example.instagram_app.screen.InstagramViewModel

@Composable
fun SinglePostScreen(
    navController: NavController,
    post: PostData,
    viewModel: InstagramViewModel
) {
    post.userId?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {
            Text(text = "Back", modifier = Modifier.clickable { navController.popBackStack() })
            CommonDivider()

            SinglePostDisplay(navController = navController, post = post, viewModel = viewModel)
        }
    }
}


@Composable
fun SinglePostDisplay(navController: NavController, viewModel: InstagramViewModel, post: PostData) {
    val userData = viewModel.userData.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = post.userImage),
                    contentDescription = null
                )

            }
            Text(text = post.userName ?: "")
            Text(text = " .", modifier = Modifier.padding(8.dp))

            if (userData?.userID == post.userId) {
                // current user post
            } else {
                Text(text = "Follow", color = Color.Blue, modifier = Modifier.clickable {
                    //follow
                })


            }

        }
    }
    Box {
        val modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
        CommonImage(
            data = post.postImage,
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )
    }
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.like),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(
                Color.Red
            )
        )
        Text(text = " ${post.likes?.size ?: 0} likes", modifier = Modifier.padding(start = 8.dp))
    }
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = post.userName ?: "", fontWeight = FontWeight.Bold)
        Text(text = post.postDescription ?: "", modifier = Modifier.padding(start = 8.dp))
    }
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = "10 comments", color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
    }
}