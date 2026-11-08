package com.portfolio.chitchat.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@Composable
fun ChatInputBar() {
    var textState by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Main Rounded Input Field Panel Container
        Row(
            modifier = Modifier
                .weight(1f)
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Mood, contentDescription = "Emoji Options", tint = Color.Gray)
            }

            TextField(
                value = textState,
                onValueChange = { textState = it },
                placeholder = { Text(text = "Message", color = Color.Gray, fontSize = 16.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(onClick = { }) {
                Icon(Icons.Filled.AttachFile, contentDescription = "Attach Documents", tint = Color.Gray)
            }
            if (textState.isEmpty()) {
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.PhotoCamera, contentDescription = "Camera Capture", tint = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Premium Circular Action Button (Mic/Send Toggle State)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(WhatsappTealGreen),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = "Audio Record",
                    tint = Color.White
                )
            }
        }
    }
}
