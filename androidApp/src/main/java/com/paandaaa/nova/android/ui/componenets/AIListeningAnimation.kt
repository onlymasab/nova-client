package com.paandaaa.nova.android.ui.componenets

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun AIListeningAnimation(
    modifier: Modifier = Modifier,
    dotColor: Color = Color(0xFF00BCD4),
    pulseColor: Color = Color(0x802196F3),
    isListening: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "pulse")

    val pulseAlpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    val pulseRadius by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(100.dp)
    ) {
        if (isListening) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2 * pulseRadius
                drawCircle(
                    color = pulseColor.copy(alpha = pulseAlpha),
                    radius = radius,
                    center = center
                )
            }
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
    }
}