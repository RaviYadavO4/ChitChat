package com.portfolio.chitchat.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappLightGreen

data class DummyChat(val name: String, val lastMessage: String, val time: String, val unreadCount: Int)

@Composable
fun ChatsTabScreen(onChatClicked: (String) -> Unit, onFabClicked: () -> Unit) {
    val dummyChats = listOf(
        DummyChat("Rahul Sharma", "Hey, project update?", "10:45 AM", 2),
        DummyChat("Freelance Client", "UI looks awesome! 🔥", "Yesterday", 0),
        DummyChat("Tech Group 🚀", "Amit: Let's use Kotlin Compose", "Yesterday", 5),
        DummyChat("Priya", "Call me when you're free.", "25/10/26", 0)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(dummyChats) { chat ->
                Surface(
                    onClick = { onChatClicked(chat.name) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ChatRowItem(chat)
                }
                HorizontalDivider(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(start = 76.dp)
                )
            }
        }

        FloatingActionButton(
            onClick = onFabClicked,
            containerColor = WhatsappLightGreen,
            contentColor = Color.White,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Filled.Message, contentDescription = "New Chat")
        }
    }
}


@Composable
fun ChatRowItem(chat: DummyChat) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.Gray.copy(alpha = 0.3f)))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = chat.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = chat.lastMessage, fontSize = 14.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = chat.time,
                fontSize = 12.sp,
                color = if (chat.unreadCount > 0) WhatsappLightGreen else Color.Gray,
                fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
            )
            if (chat.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(WhatsappLightGreen), contentAlignment = Alignment.Center) {
                    Text(text = chat.unreadCount.toString(), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
