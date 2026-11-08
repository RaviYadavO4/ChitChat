package com.portfolio.chitchat.ui.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun StatusViewerScreen(userName: String, onStoryTimedOut: () -> Unit) {
    var progress by remember { mutableStateOf(0.0f) }

    // Dynamic horizontal line animation tick generator loops
    LaunchedEffect(key1 = true) {
        while (progress < 1.0f) {
            delay(30L)
            progress += 0.01f
        }
        onStoryTimedOut()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Horizontal Top Custom Progress Segment Line Layout Bars
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .height(3.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f),
            )

            // User Info Details Box Row Context
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(40.dp).background(Color.LightGray, shape = RoundedCornerShape(20.dp)))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = userName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Just now", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Center Image/Media Simulation Field Area Block
            Text(
                text = "Building ChitChat Clone with Modern Tech Stack! 🚀",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Story Direct Text Message Input Form Fields
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Reply", color = Color.White.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(24.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
}
