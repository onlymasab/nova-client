package com.paandaaa.nova.android.ui.componenets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VoiceDotsAnimation(
    dotColor: Color = Color(0xFF00BCD4),
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "voiceDots")

    val delays = listOf(0, 150, 300)

    val scales = delays.map { delay ->
        transition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = LinearEasing, delayMillis = delay),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale_$delay"
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        scales.forEach { scale ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .scale(scale.value)
                    .background(dotColor, CircleShape)
            )
        }
    }
}