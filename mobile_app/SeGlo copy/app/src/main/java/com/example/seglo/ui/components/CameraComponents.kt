package com.example.seglo.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seglo.ui.theme.LocalCustomColors

@Composable
fun CameraPreviewCard(
    isCameraActive: Boolean,
    isProcessing: Boolean,
    onCameraToggle: () -> Unit,
    modifier: Modifier = Modifier,
    cameraContent: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 3.5f), // Keep this for consistent aspect ratio
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        listOf(Color.White, Color.Blue)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onCameraToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (isCameraActive) {
                cameraContent?.let {
                    Box(Modifier.fillMaxSize()) { it() }
                }
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = Color.LightGray,
                        strokeWidth = 3.dp,
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Camera Inactive",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Tap to start camera",
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CameraControlButtons(
    isCameraActive: Boolean,
    isProcessing: Boolean,
    onCameraToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onCameraToggle,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isCameraActive) Color(0xFFE53E3E) else Color(0xFF4B426A),
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = if (isCameraActive) Icons.Default.Close else Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isCameraActive) "Stop Camera" else "Start Camera",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DetectionResultsCard(
    detectedSign: String,
    confidence: Float,
    isProcessing: Boolean,
    isCameraActive: Boolean,
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = customColors.softGray),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Text(
                text = "Detection Results",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = customColors.darkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (detectedSign.isNotEmpty() && !isProcessing) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (confidence > 0.7f)
                            Color(0xFF4CAF50).copy(alpha = 0.1f)
                        else
                            Color(0xFFFF9800).copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    "Detected Sign:",
                                    fontSize = 12.sp,
                                    color = customColors.darkGray.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = detectedSign,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (confidence > 0.7f) Color(0xFF2E7D32) else Color(
                                        0xFFE65100
                                    )
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "Confidence:",
                                    fontSize = 12.sp,
                                    color = customColors.darkGray.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "${(confidence * 100).toInt()}%",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (confidence > 0.7f) Color(0xFF2E7D32) else Color(
                                        0xFFE65100
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = confidence,
                            modifier = Modifier.fillMaxWidth(),
                            color = if (confidence > 0.7f) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            trackColor = Color.LightGray.copy(alpha = 0.3f)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when {
                            isProcessing -> "Processing sign detection..."
                            isCameraActive -> "Ready for sign detection"
                            else -> "Start camera to begin detection"
                        },
                        fontSize = 14.sp,
                        color = customColors.darkGray.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}