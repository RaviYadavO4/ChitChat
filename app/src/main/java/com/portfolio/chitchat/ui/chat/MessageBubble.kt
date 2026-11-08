package com.portfolio.chitchat.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.portfolio.chitchat.data.model.Message
import com.portfolio.chitchat.ui.theme.WhatsappIncomingBubble
import com.portfolio.chitchat.ui.theme.WhatsappOutgoingBubble
import com.portfolio.chitchat.utils.AudioPlayer
import com.portfolio.chitchat.utils.ImageUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(message: Message) {
    val currentUid = remember { FirebaseAuth.getInstance().currentUser?.uid ?: "" }
    val isOutgoing = message.senderId == currentUid
    val bubbleColor = if (isOutgoing) WhatsappOutgoingBubble else WhatsappIncomingBubble
    val context = LocalContext.current

    // Audio Playback Player Lifecycle Instance Hook
    val audioPlayer = remember { AudioPlayer(context) }

    val timeLabel = remember(message.timestamp) {
        if (message.timestamp == 0L) "" else {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            sdf.format(Date(message.timestamp))
        }
    }

    val tickLabel = when (message.status) {
        "read" -> "✓✓"
        "delivered" -> "✓✓"
        else -> "✓"
    }
    val tickColor = if (message.status == "read") Color(0xFF34B7F1) else Color.Gray

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(
                        topStart = 8.dp, topEnd = 8.dp,
                        bottomStart = if (isOutgoing) 8.dp else 0.dp,
                        bottomEnd = if (isOutgoing) 0.dp else 8.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // 1. DYNAMIC RENDERING: Image Media Streams Layout Block
            if (message.type == "image" && message.fileBase64.isNotBlank()) {
                val bitmapImage = remember(message.fileBase64) {
                    ImageUtils.base64ToBitmap(message.fileBase64)
                }
                bitmapImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Base64 Shared Media Picture Thumbnail",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(bottom = 4.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // 2. DYNAMIC RENDERING: PDF Documents Interface Layout Block
            if (message.type == "pdf") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.05f), shape = RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📄", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Document.pdf", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
                        Text(text = "Base64 Verified Payload File", color = Color.Gray, fontSize = 11.sp)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // 3. DYNAMIC RENDERING: Voice Notes / Audio Playback Controller
            if (message.type == "audio" && message.fileBase64.isNotBlank()) {
                Surface(
                    onClick = { audioPlayer.playBase64Audio(message.fileBase64) },
                    color = Color.Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("▶️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Voice Note (${(message.fileBase64.length / 1024)} KB)",
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // 4. DYNAMIC RENDERING: Standard Text Messages Node Frame fallback
            if (message.text.isNotBlank() && (message.type == "text" || message.type.isBlank())) {
                Text(text = message.text, color = Color.Black, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Time Labels and Dynamic Multi-State Checkmarks Ticks
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = timeLabel, color = Color.Gray, fontSize = 10.sp)
                if (isOutgoing) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = tickLabel,
                        color = tickColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
