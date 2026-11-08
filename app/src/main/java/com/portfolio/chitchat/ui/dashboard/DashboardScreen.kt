package com.portfolio.chitchat.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onChatClicked: (String, String, Boolean) -> Unit,
    onFabClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ChitChat", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White) },
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White) }
                    Box {
                        IconButton(onClick = { showMenu = true }) { Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White) }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(text = { Text("Settings") }, onClick = { showMenu = false; onSettingsClicked() })
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
            )
        }
    ) { internalPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(internalPadding)) {
            ChatsTabScreen(onChatClicked = onChatClicked, onFabClicked = onFabClicked)
        }
    }
}
