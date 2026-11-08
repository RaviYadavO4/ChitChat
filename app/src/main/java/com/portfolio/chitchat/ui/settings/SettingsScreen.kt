package com.portfolio.chitchat.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClicked: () -> Unit,
    onAccountClicked: () -> Unit,
    onChatsClicked: () -> Unit,
    onNotificationsClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) },
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
            // User Dynamic Header Profile Info Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Handle Main Profile Edit Interaction */ }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Your Portfolio Name", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = "Hey there! I am using ChitChat.", fontSize = 14.sp, color = Color.Gray)
                }
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)

            // Settings Option Component Callbacks Integrated
            SettingsOptionRow(
                icon = Icons.Filled.AccountCircle,
                title = "Account",
                subtitle = "Security, change number",
                onClick = onAccountClicked
            )
            SettingsOptionRow(
                icon = Icons.Filled.Chat,
                title = "Chats",
                subtitle = "Theme, wallpapers, chat history",
                onClick = onChatsClicked
            )
            SettingsOptionRow(
                icon = Icons.Filled.Notifications,
                title = "Notifications",
                subtitle = "Message, group & call tones",
                onClick = onNotificationsClicked
            )
        }
    }
}

@Composable
fun SettingsOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Material standard premium touch ripple animation mapping
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Text(text = subtitle, fontSize = 13.sp, color = Color.Gray)
        }
    }
}
