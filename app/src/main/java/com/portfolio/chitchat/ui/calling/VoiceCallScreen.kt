package com.portfolio.chitchat.ui.calling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappDarkGreen

@Composable
fun VoiceCallScreen(userName: String, onEndCallClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsappDarkGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // User Meta Configuration Panel
            Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Ringing...", fontSize = 16.sp, color = Color.White.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.weight(1f))

            // Large Circular Profile Avatar Display Element
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.6f))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Premium Controls Anchor Array
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.VolumeUp, contentDescription = "Speaker", tint = Color.White, modifier = Modifier.size(28.dp))
                }

                // End Call Red Button Layout Node
                FloatingActionButton(
                    onClick = onEndCallClicked,
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(Icons.Filled.CallEnd, contentDescription = "End Call", modifier = Modifier.size(28.dp))
                }

                IconButton(onClick = { }) {
                    Icon(Icons.Filled.MicOff, contentDescription = "Mute", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}
