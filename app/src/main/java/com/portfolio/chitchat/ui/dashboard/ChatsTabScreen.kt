package com.portfolio.chitchat.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.utils.ImageUtils
import com.portfolio.chitchat.utils.shimmerEffect
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatsTabScreen(onChatClicked: (String, String) -> Unit, onFabClicked: () -> Unit) {
    val dashboardViewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val activeChatItems by dashboardViewModel.activeFeeds.collectAsState()
    val isFeedLoading by dashboardViewModel.isFeedLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (isFeedLoading) {
            // Renders premium high fidelity skeleton rows placeholders while syncing fields
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(6) { ShimmerFeedRowPlaceholder() }
            }
        } else if (activeChatItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No active conversations yet.", color = Color.Gray, fontSize = 15.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(activeChatItems) { item ->
                    Surface(onClick = { onChatClicked(item.participantUser?.name ?: "User", item.participantUser?.uid ?: "") }, modifier = Modifier.fillMaxWidth()) {
                        ChatFeedRow(item)
                    }
                }
            }
        }

        FloatingActionButton(onClick = onFabClicked, containerColor = com.portfolio.chitchat.ui.theme.WhatsappLightGreen, contentColor = Color.White, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp), shape = CircleShape) {
            Icon(Icons.Filled.Message, contentDescription = "Msg")
        }
    }
}

@Composable
fun ChatFeedRow(item: ActiveFeedItem) {
    val userMeta = item.participantUser
    val chatLog = item.chatMeta

    val timeLabel = remember(chatLog.lastMessageTimestamp) {
        if (chatLog.lastMessageTimestamp == 0L) "" else {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            sdf.format(Date(chatLog.lastMessageTimestamp))
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dynamic Base64 Avatar Profile Rendering System
        if (userMeta != null && userMeta.profileImageBase64.isNotBlank()) {
            val profileBitmap = remember(userMeta.profileImageBase64) {
                ImageUtils.base64ToBitmap(userMeta.profileImageBase64)
            }
            profileBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Profile Picture Feed Avatar",
                    modifier = Modifier.size(50.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.Gray.copy(alpha = 0.3f)))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = userMeta?.name ?: "ChitChat User", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chatLog.lastMessage,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = timeLabel, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
@Composable
fun ShimmerFeedRowPlaceholder() {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(50.dp).clip(CircleShape).shimmerEffect())
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(16.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(12.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
        }
    }
}
