package com.example.seglo.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import kotlin.math.max
import kotlin.math.min

@Composable
fun HandLandmarkOverlay(
    result: HandLandmarkerResult,
    imageWidth: Int,
    imageHeight: Int,
    runningMode: RunningMode = RunningMode.LIVE_STREAM,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val scale = when (runningMode) {
            RunningMode.IMAGE, RunningMode.VIDEO ->
                min(size.width / imageWidth, size.height / imageHeight)
            RunningMode.LIVE_STREAM ->
                max(size.width / imageWidth, size.height / imageHeight)
            else -> 1f
        }
        val offsetX = (size.width - imageWidth * scale) / 2f
        val offsetY = (size.height - imageHeight * scale) / 2f

        // Draw each detected hand with a different color for clarity
        val handColors = listOf(Color.Yellow, Color.Cyan)
        val connectionColors = listOf(Color(0xFF018786), Color(0xFF0057B7))

        result.landmarks().forEachIndexed { handIdx, landmarkList ->
            val pointColor = handColors.getOrElse(handIdx) { Color.Magenta }
            val lineColor = connectionColors.getOrElse(handIdx) { Color.Gray }

            // Draw landmarks
            for (normalizedLandmark in landmarkList) {
                drawCircle(
                    color = pointColor,
                    radius = 8f,
                    center = Offset(
                        normalizedLandmark.x() * imageWidth * scale + offsetX,
                        normalizedLandmark.y() * imageHeight * scale + offsetY
                    )
                )
            }
            // Draw connections
            HandLandmarker.HAND_CONNECTIONS.forEach { connection ->
                val start = landmarkList[connection!!.start()]
                val end = landmarkList[connection.end()]
                drawLine(
                    color = lineColor,
                    start = Offset(
                        start.x() * imageWidth * scale + offsetX,
                        start.y() * imageHeight * scale + offsetY
                    ),
                    end = Offset(
                        end.x() * imageWidth * scale + offsetX,
                        end.y() * imageHeight * scale + offsetY
                    ),
                    strokeWidth = 8f
                )
            }
        }
    }
}