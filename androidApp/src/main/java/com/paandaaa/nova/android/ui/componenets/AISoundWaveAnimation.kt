package com.paandaaa.nova.android.ui.componenets

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun AiSoundWaveAnimation(
    modifier: Modifier = Modifier,
    barCount: Int = 30,
    primaryColor: Color = Color(0xFF4285F4),
    secondaryColor: Color = Color(0xFF34A853),
    activeColor: Color = Color(0xFFEA4335),
    idleAmplitude: Float = 20f,
    animationDuration: Int = 1500
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Animation for the wave movement
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing)
        )
    )

    // Animation for random peaks
    val peakAnimations = List(barCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = animationDuration * 2
                    0f at 0 with LinearEasing
                    0f at randomDelay(index, barCount, animationDuration * 2)
                    1f at (randomDelay(index, barCount, animationDuration * 2) + 200) with FastOutSlowInEasing
                    0f at (randomDelay(index, barCount, animationDuration * 2) + 400) with LinearEasing
                },
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = modifier.fillMaxWidth().height(100.dp)) {
        val width = size.width
        val height = size.height
        val barWidth = width / (barCount * 2f)
        val spacing = barWidth / 2

        for (i in 0 until barCount) {
            // Calculate position and amplitude
            val x = spacing + i * (barWidth + spacing)
            val position = i.toFloat() / barCount
            val dynamicAmplitude = idleAmplitude * (0.7f + 0.3f * sin(phase + position * 4f))

            // Apply peak animation
            val peakAmplitude = peakAnimations[i].value * idleAmplitude * 1.5f
            val totalAmplitude = dynamicAmplitude + peakAmplitude

            // Choose color based on activity
            val color = when {
                peakAnimations[i].value > 0.1f -> activeColor
                i % 3 == 0 -> primaryColor
                else -> secondaryColor
            }

            // Draw the bar
            drawLine(
                color = color,
                start = Offset(x, height / 2 - totalAmplitude),
                end = Offset(x, height / 2 + totalAmplitude),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun randomDelay(index: Int, count: Int, maxDuration: Int): Int {
    // Distribute peaks randomly but evenly
    val baseDelay = (index * maxDuration / count).toFloat()
    val randomOffset = (0.3f * maxDuration / count * (Math.random().toFloat() - 0.5f))
    return (baseDelay + randomOffset).toInt()
}

@Preview
@Composable
fun AiSoundWavePreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        AiSoundWaveAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}