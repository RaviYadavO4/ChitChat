package com.portfolio.chitchat.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

// Message Model Data Structure
data class DummyMessage(val id: String, val text: String, val isOutgoing: Boolean, val timestamp: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(userName: String, onBackClicked: () -> Unit) {
    val dummyMessages = listOf(
        DummyMessage("1", "Hey! Kaise ho?", false, "10:00 AM"),
        DummyMessage("2", "Main badhiya hoon, aap batao? Project chal raha hai?", true, "10:01 AM"),
        DummyMessage("3", "Haan, UI almost ready hai modern MVVM aur Compose ke sath! 🔥", false, "10:02 AM"),
        DummyMessage("4", "Zabardast! Freelancing me client ko bohot pasand aayega.", true, "10:03 AM")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = userName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "online", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.VideoCall, contentDescription = "Video Call", tint = Color.White)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Call, contentDescription = "Voice Call", tint = Color.White)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Options", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
            )
        },
        bottomBar = {
            ChatInputBar()
        }
    ) { paddingValues ->
        // Classic WhatsApp wallpaper background color tint structure
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFECE5DD))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dummyMessages) { message ->
                    MessageBubble(message = message)
                }
            }
        }
    }
}
