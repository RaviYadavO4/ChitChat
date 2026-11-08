package com.portfolio.chitchat.ui.settings.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.settings.SettingsOptionRow
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSettingsScreen(onBackClicked: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Notifications", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            SettingsOptionRow(
                icon = Icons.Filled.Message,
                title = "Conversation tones",
                subtitle = "Play sounds for incoming messages",
                onClick = {}
            )
            SettingsOptionRow(
                icon = Icons.Filled.Vibration,
                title = "Vibrate",
                subtitle = "Default / Short / Long patterns",
                onClick = {}
            )
            SettingsOptionRow(
                icon = Icons.Filled.Message,
                title = "Group alerts",
                subtitle = "Manage specific tones for group chats",
                onClick = {}
            )
        }
    }
}
