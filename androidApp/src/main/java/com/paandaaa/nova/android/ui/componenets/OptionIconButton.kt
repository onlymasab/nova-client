package com.paandaaa.nova.android.ui.componenets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OptionIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    isActive: Boolean = false,
    isSignOut: Boolean = false,
    text: String? = null
) {
    val containerColor = when {
        isSignOut -> Color.Red
        isActive -> Color.White
        else -> Color.White.copy(alpha = 1f)
    }
    val contentColor = when {
        isSignOut -> Color.White
        isActive -> Color.Black
        else -> Color.Gray
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            colors = IconButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.Gray
            ),
            modifier = Modifier.size(52.dp)
        ) {
            Icon(icon, contentDescription = "Button Icon", modifier = Modifier.size(24.dp))
        }
        Text(
            text = text ?: "",
            style = TextStyle(fontSize = 14.sp, color = contentColor),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}