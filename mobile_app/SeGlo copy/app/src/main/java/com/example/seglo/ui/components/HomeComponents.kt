package com.example.seglo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seglo.R
import com.example.seglo.ui.theme.LocalCustomColors
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.seglo.ui.theme.LocalIsDarkTheme

@Composable
fun WelcomeCard(
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    val isDark = LocalIsDarkTheme.current
    val headerRes = if (isDark) R.drawable.header_darkmode else R.drawable.header

    // Define colors based on theme
    val hiUserColor = if (isDark) Color(0xFFFCFAE7) else customColors.independenceBlue
    val welcomeColor = if (isDark) Color.White else customColors.lavenderGray

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp)
            .border(
                width = 2.dp,
                color = Color(0xFFBDBDB7),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Background Image
        Image(
            painter = painterResource(id = headerRes),
            contentDescription = "Welcome Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Text Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hi user,",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = hiUserColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Welcome to SeGlo! Your Personal Sign Language\nTranslation App",
                fontSize = 12.sp,
                color = welcomeColor,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun BluetoothStatusCard(
    isConnected: Boolean,
    onConnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    // Define colors
    val customColors = LocalCustomColors.current
    val statusBorderColor = if (isConnected) Color(0xFF7C9717) else Color(0xFFCD8800)
    val statusBackgroundColor = if (isConnected) Color(0xFFD0DC8E) else Color(0xFFE2C592)
    val statusTextColor = if (isConnected) Color(0xFF8CB264) else Color(0xFF845E12)
    val statusCircleColor = if (isConnected) Color(0xFF2D924B) else Color(0xFFC37800)
    val cardBackground = if (isDark) Color(0xFF303053) else customColors.lightGrayBackground
    val textColor = if (isDark) Color.White else customColors.darkGray

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Bluetooth",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status:",
                    fontSize = 14.sp,
                    color = textColor
                )

                Card(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = statusBorderColor,
                            shape = RoundedCornerShape(6.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = statusBackgroundColor
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = statusCircleColor,
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isConnected) "Connected" else "Disconnected",
                            color = statusTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onConnectClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = customColors.connectButtonColor,
                    contentColor = customColors.darkGray
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = if (isConnected) "Disconnect" else "Connect",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    val languages = listOf(
        "English",
        "Tagalog",
        "Korean",
        "Chinese (Simplified)",
        "Japanese",
        "Spanish",
        "French",
        "Italian"
    )

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Select Language",
            fontSize = 14.sp,
            color = customColors.darkGray.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedLanguage,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Language Selector"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = customColors.accentBlue,
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(language) },
                        onClick = {
                            onLanguageSelected(language)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MessageTextBox(
    message: String,
    onMessageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Message",
            fontSize = 14.sp,
            color = customColors.darkGray.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = customColors.accentBlue,
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = "Sign language output will appear here...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        )
    }
}

@Composable
fun SpeechControls(
    onSpeakClick: () -> Unit,
    onStopSpeakClick: () -> Unit,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onSpeakClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSpeaking) customColors.darkGray else customColors.speakButtonColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = !isSpeaking
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Speak",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Speak",
                fontSize = 12.sp
            )
        }

        Button(
            onClick = onStopSpeakClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSpeaking) Color(0xFFFF5722) else Color.Gray,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = isSpeaking
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Stop Speak",
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TranslationBox(
    translatedText: String,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    editable: Boolean = false,
    onTextChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(220.dp)
    ) {
        OutlinedTextField(
            value = translatedText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxSize(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = customColors.accentBlue,
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = "Enter a phrase or make a sign...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onCopyClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = customColors.darkGray,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onShareClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = customColors.darkGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SensorToggle(
    showSensorValues: Boolean,
    onToggleChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onToggleChange(!showSensorValues) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Show Sensor Values",
            fontSize = 14.sp,
            color = customColors.darkGray
        )

        Switch(
            checked = showSensorValues,
            onCheckedChange = onToggleChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = customColors.deepPurple,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}

@Composable
fun SensorValuesDisplay(
    flexSensorValues: Map<String, Float>,
    gyroSensorValues: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = customColors.softGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Sensor Values",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = customColors.deepPurple
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Flex Sensor Values
            Text(
                text = "Flex Sensors:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = customColors.darkGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            flexSensorValues.forEach { (finger, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$finger:",
                        fontSize = 12.sp,
                        color = customColors.darkGray.copy(alpha = 0.8f)
                    )
                    Text(
                        text = String.format("%.2f", value),
                        fontSize = 12.sp,
                        color = customColors.deepPurple,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Gyro Sensor Values
            Text(
                text = "Gyro Sensor:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = customColors.darkGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            gyroSensorValues.forEach { (axis, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$axis:",
                        fontSize = 12.sp,
                        color = customColors.darkGray.copy(alpha = 0.8f)
                    )
                    Text(
                        text = String.format("%.2f", value),
                        fontSize = 12.sp,
                        color = customColors.deepPurple,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AssignmentControl(
    showButtons: Boolean,
    onToggleChange: (Boolean) -> Unit,
    onButtonClick: (String, String) -> Unit,
    showToast: (String) -> Unit,
    assignmentText: String,
    onAssignmentTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstModeLabels = listOf("01", "02", "03", "04", "05")
    val secondModeLabels = listOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15")

    Column(modifier = modifier.fillMaxWidth()) {
        // Toggle row (always visible)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Configure Assignments")
            Switch(
                checked = showButtons,
                onCheckedChange = onToggleChange
            )
        }
        // Button rows (only visible when toggled)
        if (showButtons) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val maxChar = 70
                OutlinedTextField(
                    value = assignmentText,
                    onValueChange = {
                        if (it.length <= maxChar) onAssignmentTextChange(it)
                    },
                    label = { Text("Text to assign (max $maxChar chars)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    minLines = 2,
                    supportingText = {
                        val remaining = maxChar - assignmentText.length
                        Text(
                            text = "Remaining: $remaining/$maxChar",
                            color = if (remaining == 0) Color.Red else Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "First Mode Gestures (Phrase)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                val firstModeChunks = firstModeLabels.chunked(5)
                firstModeChunks.forEach { rowLabels ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowLabels.forEach { label ->
                            Button(
                                onClick = {
                                    if (assignmentText.isNotEmpty()) {
                                        onButtonClick("FirstMode-$label", assignmentText)
                                    } else {
                                        showToast("Enter value first to assign for the selected M1 gesture.")
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        repeat(5 - rowLabels.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Second Mode Gestures (Letter)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                val secondModeChunks = secondModeLabels.chunked(5)
                secondModeChunks.forEach { rowLabels ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowLabels.forEach { label ->
                            Button(
                                onClick = {
                                    if (assignmentText.isNotEmpty()) {
                                        onButtonClick("SecondMode-$label", assignmentText)
                                    } else {
                                        showToast("Enter value first to assign for the selected M2 gesture.")
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        repeat(5 - rowLabels.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}