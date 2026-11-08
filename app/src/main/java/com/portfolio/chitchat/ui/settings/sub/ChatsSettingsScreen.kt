package com.portfolio.chitchat.ui.settings.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.settings.SettingsOptionRow
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsSettingsScreen(
    onBackClicked: () -> Unit,
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    val currentThemeSubtitle = if (isDarkMode) "Dark" else "Light"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chats", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background) // Dynamic surface tint
        ) {
            Text(
                text = "Display",
                fontSize = 14.sp,
                color = WhatsappTealGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            SettingsOptionRow(
                icon = Icons.Filled.Palette,
                title = "Theme",
                subtitle = currentThemeSubtitle,
                onClick = { showThemeDialog = true }
            )
            SettingsOptionRow(
                icon = Icons.Filled.Wallpaper,
                title = "Wallpaper",
                subtitle = "Choose custom chat backgrounds",
                onClick = { }
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Chat settings",
                fontSize = 14.sp,
                color = WhatsappTealGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            SettingsOptionRow(icon = Icons.Filled.FontDownload, title = "Font size", subtitle = "Medium", onClick = {})
            SettingsOptionRow(icon = Icons.Filled.Backup, title = "Chat backup", subtitle = "Backup messages to cloud database", onClick = {})
            SettingsOptionRow(icon = Icons.Filled.History, title = "Chat history", subtitle = "Export, clear, or archive all conversation logs", onClick = {})
        }

        // WhatsApp Style Live Active Theme Selection Dialog
        if (showThemeDialog) {
            AlertDialog(
                onDismissRequest = { showThemeDialog = false },
                title = { Text(text = "Choose theme", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        // Light Mode Selection Trigger Option Row
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            RadioButton(
                                selected = !isDarkMode,
                                onClick = {
                                    onThemeChanged(false) // Toggle true runtime Light theme updates
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Light", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        }
                        // Dark Mode Selection Trigger Option Row
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            RadioButton(
                                selected = isDarkMode,
                                onClick = {
                                    onThemeChanged(true) // Toggle true runtime Dark theme updates
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Dark", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showThemeDialog = false }) {
                        Text("CANCEL", color = WhatsappTealGreen, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
