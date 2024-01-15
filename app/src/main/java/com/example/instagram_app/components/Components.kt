package com.example.instagram_app.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.instagram_app.R
import com.example.instagram_app.screen.InstagramViewModel

@Composable
fun NotificationMessage(viewModel: InstagramViewModel) {
    val notifState = viewModel.popUpNotification.value
    val notiMessage = notifState?.getContentOrNull()
    if (notiMessage != null) {
        Toast.makeText(LocalContext.current, notiMessage, Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun CommonProgressSpinner() {
    Row(modifier = Modifier
        .alpha(0.5f)
        .background(Color.LightGray)
        .clickable(enabled = false) {}
        .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator()
    }
}

@Composable
fun AppLogo(text: String) {
    Image(
        painter = painterResource(id = R.drawable.ig_logo),
        contentDescription = null,
        modifier = Modifier
            .width(250.dp)
            .padding(top = 16.dp)
            .padding(8.dp)
    )
    Text(
        text = text,
        modifier = Modifier.padding(8.dp),
        fontSize = 30.sp,
        fontFamily = FontFamily.SansSerif
    )
}

@Composable
fun InputTextField(value: MutableState<TextFieldValue>, text: String) {
    OutlinedTextField(value = value.value, onValueChange = {
        value.value = it
    }, modifier = Modifier.padding(8.dp), label = {
        Text(text = text)
    })
}

@Composable
fun InputPasswordField(
    value: MutableState<TextFieldValue>, eye: MutableState<Boolean>
) {
    val visualTransformation =
        if (eye.value) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(value = value.value, onValueChange = {
        value.value = it
    }, modifier = Modifier.padding(8.dp), label = {
        Text(text = "Password")
    }, visualTransformation = visualTransformation, trailingIcon = {
        IconButton(onClick = { eye.value = !eye.value }) {
            if (eye.value) Icon(
                painter = painterResource(id = R.drawable.visibilityoff),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            else Icon(
                painter = painterResource(id = R.drawable.visibility),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    })
}

@Composable
fun CustomButton(onClick: () -> Unit, text: String) {
    Button(onClick = onClick, modifier = Modifier.padding(8.dp)) {
        Text(text = text)
    }
}

@Composable
fun CustomTextButton(onClick: () -> Unit, text: String) {
    Text(text = text, color = Color.Blue, modifier = Modifier
        .padding(8.dp)
        .clickable {
            onClick.invoke()
        })
}

@Composable
fun CommonImage(
    data: String?,
    modifier: Modifier = Modifier.wrapContentSize(),
    contentScale: ContentScale = ContentScale.Crop
) {
    val painter = rememberAsyncImagePainter(model = data)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )

    if (painter.state is AsyncImagePainter.State.Loading) {
        CommonProgressSpinner()
    }

}

@Composable
fun UserImageCard(
    userImage: String?, modifier: Modifier = Modifier
        .padding(8.dp)
        .size(64.dp)
) {
    Card(shape = CircleShape, modifier = modifier) {
        if (userImage.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.person),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.Gray)
            )
        } else {
            CommonImage(data = userImage)
        }
    }
}