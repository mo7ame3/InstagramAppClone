package com.example.instagram_app.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagram_app.components.CommonDivider
import com.example.instagram_app.components.CommonProgressSpinner
import com.example.instagram_app.navigation.AllScreens
import com.example.instagram_app.screen.InstagramViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: InstagramViewModel) {
    val isLoading = viewModel.inProgress.value
    if (isLoading) {
        CommonProgressSpinner()
    } else {
        val userData = viewModel.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        var userName by rememberSaveable {
            mutableStateOf(userData?.userName ?: "")
        }
        var bio by rememberSaveable {
            mutableStateOf(userData?.bio ?: "")
        }

        ProfileContent(
            viewModel = viewModel,
            name = name,
            userName = userName,
            bio = bio,
            onNameChange = { name = it },
            onUserNameChange = { userName = it },
            onBioChange = { bio = it },
            onSave = {},
            onBack = {
                navController.navigate(route = AllScreens.PostScreen.name) {
                    navController.popBackStack()
                }
            },
            onLogout = { viewModel.signOut(navController = navController) }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    viewModel: InstagramViewModel,
    name: String,
    userName: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })
        }
        CommonDivider()

        // userImage

        Column(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {

        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "UserName", modifier = Modifier.width(100.dp))
            TextField(
                value = userName,
                onValueChange = onUserNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp), verticalAlignment = Alignment.Top
        ) {
            Text(text = "bio", modifier = Modifier.width(100.dp))
            TextField(
                value = bio,
                onValueChange = onBioChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Logout", modifier = Modifier.clickable { onLogout.invoke() })
        }

    }
}
