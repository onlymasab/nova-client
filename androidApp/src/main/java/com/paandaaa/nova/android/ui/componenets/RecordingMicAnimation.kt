package com.paandaaa.nova.android.ui.componenets

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun RecordingMicAnimation(
    isRecording: Boolean,
    modifier: Modifier = Modifier,
    baseColor: Color = Color.Red,
    pulseColor: Color = Color(0x55FF1744)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "micPulse")

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius"
    )

    Box(
        modifier = modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isRecording) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2 * pulseRadius
                drawCircle(
                    color = pulseColor.copy(alpha = pulseAlpha),
                    radius = radius,
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(baseColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Recording",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}