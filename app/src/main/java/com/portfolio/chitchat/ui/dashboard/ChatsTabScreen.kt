package com.portfolio.chitchat.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
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
fun ChatsTabScreen(onChatClicked: (String, String, Boolean) -> Unit, onFabClicked: () -> Unit) {
    val dashboardViewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val activeChatItems by dashboardViewModel.activeFeeds.collectAsState()
    val isFeedLoading by dashboardViewModel.isFeedLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        AnimatedVisibility(
            visible = isFeedLoading,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(6) { ShimmerFeedRowPlaceholder() }
            }
        }

        AnimatedVisibility(
            visible = !isFeedLoading && activeChatItems.isEmpty(),
            enter = fadeIn(animationSpec = tween(400))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Message, contentDescription = null, tint = Color.Gray.copy(alpha = 0.5f), modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "No active conversations yet.", color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        AnimatedVisibility(
            visible = !isFeedLoading && activeChatItems.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(400))
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(activeChatItems, key = { it.chatMeta.chatId }) { item ->
                    val isGroup = item.chatMeta.isGroup
                    val title = if (isGroup) item.chatMeta.groupName else (item.participantUser?.name ?: "User")
                    val id = if (isGroup) item.chatMeta.chatId else (item.participantUser?.uid ?: "")

                    Surface(
                        onClick = { onChatClicked(title, id, isGroup) },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        ChatFeedRow(item)
                    }
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f), thickness = 0.5.dp)
                }
            }
        }

        FloatingActionButton(
            onClick = onFabClicked,
            containerColor = com.portfolio.chitchat.ui.theme.WhatsappLightGreen,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Filled.Message, contentDescription = "New Chat", modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun ChatFeedRow(item: ActiveFeedItem) {
    val userMeta = item.participantUser
    val chatLog = item.chatMeta
    val isGroup = chatLog.isGroup

    val timeLabel = remember(chatLog.lastMessageTimestamp) {
        if (chatLog.lastMessageTimestamp == 0L) "" else {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            sdf.format(Date(chatLog.lastMessageTimestamp))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isGroup) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(com.portfolio.chitchat.ui.theme.WhatsappTealGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Group, contentDescription = "Group", tint = Color.White, modifier = Modifier.size(26.dp))
            }
        } else if (userMeta != null && userMeta.profileImageBase64.isNotBlank()) {
            val profileBitmap = remember(userMeta.profileImageBase64) {
                ImageUtils.base64ToBitmap(userMeta.profileImageBase64)
            }
            profileBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(52.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = (userMeta?.name?.take(1) ?: "U").uppercase(), fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isGroup) chatLog.groupName else (userMeta?.name ?: "ChitChat User"),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
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
        Box(modifier = Modifier.size(52.dp).clip(CircleShape).shimmerEffect())
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(16.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(12.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
        }
    }
}
