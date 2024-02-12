package com.example.instagram_app.screen.feed

import PostData
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagram_app.components.BottomNavigationItem
import com.example.instagram_app.components.BottomNavigationMenu
import com.example.instagram_app.components.CommonImage
import com.example.instagram_app.components.CommonProgressSpinner
import com.example.instagram_app.components.LikeAnimation
import com.example.instagram_app.components.UserImageCard
import com.example.instagram_app.navigation.AllScreens
import com.example.instagram_app.screen.InstagramViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FeedScreen(navController: NavController, viewModel: InstagramViewModel) {
    val userDataLoading = viewModel.inProgress.value
    val userData = viewModel.userData.value
    val personalizedFeed = viewModel.postsFeed.value
    val personalizedFeedLoading = viewModel.postsFeedProgress.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
        ) {
            UserImageCard(userImage = userData?.imageUrl)

        }
        PostsList(
            posts = personalizedFeed,
            modifier = Modifier.weight(1f),
            loading = personalizedFeedLoading or userDataLoading,
            navController = navController,
            viewModel = viewModel,
            currentUserId = userData?.userID ?: ""
        )
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.FEED,
            navController = navController
        )


    }

}

@Composable
fun PostsList(
    posts: List<PostData>,
    modifier: Modifier,
    loading: Boolean,
    navController: NavController,
    viewModel: InstagramViewModel,
    currentUserId: String
) {
    Box(modifier = modifier) {
        LazyColumn {
            items(items = posts) { post ->
                Post(
                    post = post,
                    currentUserId = currentUserId,
                    viewModel = viewModel,
                ) {
                    val postJson = post.toJson()
                    val encodedPostJson =
                        URLEncoder.encode(postJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(route = AllScreens.SinglePostScreen.name + "/$encodedPostJson")
                }
            }
        }
        if (loading)
            CommonProgressSpinner()
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Post(
    post: PostData,
    currentUserId: String,
    viewModel: InstagramViewModel,
    onPostClick: () -> Unit
) {
    val likeAnimation = remember { mutableStateOf(false) }
    val disLikeAnimation = remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape, modifier = Modifier
                        .padding(4.dp)
                        .size(32.dp)
                ) {
                    CommonImage(data = post.userImage, contentScale = ContentScale.Crop)
                }
                Text(text = post.userName ?: "", modifier = Modifier.padding(4.dp))
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (post.likes?.contains(currentUserId) == true) {
                                    disLikeAnimation.value = true
                                } else {
                                    likeAnimation.value = true
                                }
                                viewModel.onLikePost(post)
                            },
                            onTap = {
                                onPostClick.invoke()
                            }
                        )
                    }
                CommonImage(
                    data = post.postImage,
                    modifier = modifier,
                    contentScale = ContentScale.FillWidth
                )

                if (likeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000L)
                        likeAnimation.value = false
                    }
                    LikeAnimation()
                }
                if (disLikeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000L)
                        disLikeAnimation.value = false
                    }
                    LikeAnimation(false)
                }
            }
        }
    }
}