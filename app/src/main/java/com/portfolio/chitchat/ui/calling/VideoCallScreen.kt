package com.portfolio.chitchat.ui.calling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.SwitchVideo
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VideoCallScreen(userName: String, onEndCallClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        // Main Screen: Remote Video Stream Simulation Frame
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = userName, fontSize = 20.sp, color = Color.White)
            Text(text = "00:05", fontSize = 14.sp, color = Color.White.copy(alpha = 0.6f))
        }

        // Sub Screen: Local Camera Floating Box Grid Coordinate Setup
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 90.dp, end = 24.dp)
                .size(width = 100.dp, height = 150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray)
        )

        // Overlay Interactive Button Controllers Block
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.SwitchVideo, contentDescription = "Switch Camera", tint = Color.White, modifier = Modifier.size(28.dp))
            }

            FloatingActionButton(
                onClick = onEndCallClicked,
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(Icons.Filled.CallEnd, contentDescription = "Disconnect Video Call", modifier = Modifier.size(28.dp))
            }

            IconButton(onClick = { }) {
                Icon(Icons.Filled.VideocamOff, contentDescription = "Camera Toggle", tint = Color.White, modifier = Modifier.size(28.dp))
            }
        }
    }
}
