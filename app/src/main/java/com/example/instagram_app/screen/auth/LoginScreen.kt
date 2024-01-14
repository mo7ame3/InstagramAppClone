package com.example.instagram_app.screen.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.example.instagram_app.components.AppLogo
import com.example.instagram_app.components.CommonProgressSpinner
import com.example.instagram_app.components.CustomButton
import com.example.instagram_app.components.CustomTextButton
import com.example.instagram_app.components.InputPasswordField
import com.example.instagram_app.components.InputTextField
import com.example.instagram_app.screen.InstagramViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: InstagramViewModel) {
    val focus = LocalFocusManager.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }
            val eye = remember { mutableStateOf(false) }

            AppLogo(text = "Login")

            InputTextField(value = emailState, text = "Email")

            InputPasswordField(value = passwordState, eye = eye)

            CustomButton(onClick = {
                focus.clearFocus(force = true)
                viewModel.onLogin(
                    email = emailState.value.text,
                    password = passwordState.value.text
                )
            }, text = "LOGIN")

            CustomTextButton(onClick = {
                navController.popBackStack()
            }, text = "New Here? Go To signup ->")
        }
        val isLoading = viewModel.inProgress.value
        if (isLoading) {
            CommonProgressSpinner()
        }
    }
}