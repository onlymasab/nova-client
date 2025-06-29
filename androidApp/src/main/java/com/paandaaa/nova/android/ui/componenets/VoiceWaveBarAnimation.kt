package com.paandaaa.nova.android.ui.componenets

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@Composable
fun AudioWaveformAnimation(
    activeColor: Color = Color(0xFF00C853),
    inactiveColor: Color = activeColor.copy(alpha = 0.3f),
    waveformCount: Int = 40, // More bars for smoother waveform
    modifier: Modifier = Modifier,
    isRecording: Boolean = true,
    sensitivity: Float = 0.7f,
    maxAmplitude: Float = 1f,
    minAmplitude: Float = 0.05f
) {
    // Store waveform data with smoothing
    val waveforms = remember {
        List(waveformCount) {
            Animatable(minAmplitude).apply {
                updateBounds(minAmplitude, maxAmplitude)
            }
        }
    }

    // Store previous values for smoothing
    val previousValues = remember { FloatArray(waveformCount) { minAmplitude } }

    // Animation parameters
    val animationDuration = 100 // Faster updates for more fluid motion
    val smoothingFactor = 0.3f // How much to smooth between values

    // Simulate audio input with different frequency bands
    val frequencyBands = remember { MutableList(5) { Random.nextFloat() } }

    LaunchedEffect(isRecording) {
        if (!isRecording) {
            // Animate all waveforms to bottom when recording stops
            waveforms.forEachIndexed { index, anim ->
                anim.animateTo(
                    targetValue = minAmplitude, // TODO: Animate this
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                )
            }
            return@LaunchedEffect
        }

        while (isRecording) {
            // Update frequency bands occasionally
            if (Random.nextFloat() > 0.9f) {
                frequencyBands.forEachIndexed { i, _ ->
                    frequencyBands[i] = Random.nextFloat()
                }
            }

            waveforms.forEachIndexed { index, anim ->
                // Calculate position in frequency spectrum (simulated)
                val bandIndex = (index % frequencyBands.size).toInt()
                val bandWeight = 0.5f + (index % frequencyBands.size) * 0.1f

                // Generate base value from frequency band with some randomness
                val baseValue = frequencyBands[bandIndex] *
                        sensitivity *
                        bandWeight *
                        (0.9f + Random.nextFloat() * 0.2f)

                // Apply smoothing to avoid abrupt changes
                val smoothedValue = previousValues[index] * (1f - smoothingFactor) +
                        baseValue * smoothingFactor

                previousValues[index] = smoothedValue

                // Apply a wave-like pattern to the values
                val wavePattern = 0.5f + 0.5f *
                        kotlin.math.sin(index * 0.3f + System.currentTimeMillis() * 0.003f)

                val targetValue = (smoothedValue * wavePattern)
                    .coerceIn(minAmplitude, maxAmplitude)

                // Animate to the new value
                anim.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(animationDuration, easing = LinearEasing)
                )
            }
            delay(30) // Faster update for smoother animation
        }
    }

    Canvas(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val barWidth = size.width / (waveformCount * 1.5f)
        val barSpacing = barWidth * 0.4f

        // Draw inactive waveform background
        for (i in 0 until waveformCount) {
            val left = i * (barWidth + barSpacing)
            drawRoundRect(
                color = inactiveColor,
                topLeft = Offset(left, size.height * (1f - minAmplitude)),
                size = Size(barWidth, size.height * minAmplitude),
                cornerRadius = CornerRadius(barWidth / 2)
            )
        }

        // Draw active waveform
        waveforms.forEachIndexed { index, anim ->
            val height = size.height * anim.value
            val left = index * (barWidth + barSpacing)

            // Draw the bar with rounded top
            drawRoundRect(
                color = activeColor,
                topLeft = Offset(left, size.height - height),
                size = Size(barWidth, height),
                cornerRadius = CornerRadius(barWidth / 2)
            )

            // Optional: Add a highlight for more depth
            if (height > size.height * 0.1f) {
                drawRoundRect(
                    color = activeColor.copy(alpha = 0.7f),
                    topLeft = Offset(left + barWidth * 0.2f, size.height - height + barWidth * 0.2f),
                    size = Size(barWidth * 0.6f, max(barWidth * 0.6f, height - barWidth * 0.4f)),
                    cornerRadius = CornerRadius(barWidth / 3),
                    style = Stroke(width = barWidth * 0.15f)
                )
            }
        }
    }
}