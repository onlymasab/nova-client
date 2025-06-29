package com.paandaaa.nova.android.ui.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.paandaaa.nova.android.R


@Composable
fun UserProfile(name: String , email: String, profilePictureUrl: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
    ) {
        UserAvatar(
            url = profilePictureUrl.replace(oldValue =  "\"", newValue = ""),
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFF3F49B))
                .size(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = name.replace(oldValue =  "\"", newValue = ""),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            )
            Text(
                text = email,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            )

        }
    }
}


@Composable
fun UserAvatar(url: String?, modifier: Modifier = Modifier) {
    if (url.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.user_avatar),
            contentDescription = "Default Avatar",
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFF3F49B), CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = "User Profile Picture",
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFF3F49B), CircleShape),
            placeholder = painterResource(R.drawable.user_avatar),
            error = painterResource(R.drawable.user_avatar),
            contentScale = ContentScale.Crop
        )
    }
}