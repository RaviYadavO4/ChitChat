package com.portfolio.chitchat.ui.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.data.model.User
import com.portfolio.chitchat.data.repository.ContactRepository
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen
import com.portfolio.chitchat.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectContactScreen(
    onBackClicked: () -> Unit,
    onContactSelected: (String, String) -> Unit
) {
    val repository = remember { ContactRepository() }
    var contactsList by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        repository.fetchAllChitChatContacts { fetched ->
            contactsList = fetched
            isLoading = false
        }
    }

    val filteredContacts = remember(searchQuery, contactsList) {
        if (searchQuery.isBlank()) contactsList else {
            contactsList.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search contacts...", color = Color.White.copy(alpha = 0.6f)) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column {
                            Text(text = "Select contact", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "${contactsList.size} contacts", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { if (isSearchActive) { isSearchActive = false; searchQuery = "" } else onBackClicked() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { isSearchActive = !isSearchActive; if (!isSearchActive) searchQuery = "" }) {
                        Icon(imageVector = if (isSearchActive) Icons.Filled.Close else Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = WhatsappTealGreen)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (!isSearchActive || searchQuery.isBlank()) {
                        item {
                            ActionContactRow(icon = Icons.Filled.PersonAdd, title = "New contact", onClick = { })
                            Text(text = "Contacts on ChitChat", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                        }
                    }

                    items(filteredContacts) { contact ->
                        Surface(
                            onClick = { onContactSelected(contact.name, contact.uid) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LiveContactRow(contact)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveContactRow(user: User) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (user.profileImageBase64.isNotBlank()) {
            val bitmap = remember(user.profileImageBase64) { ImageUtils.base64ToBitmap(user.profileImageBase64) }
            bitmap?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = "Avatar", modifier = Modifier.size(40.dp).clip(CircleShape), contentScale = ContentScale.Crop)
            }
        } else {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = user.statusText, fontSize = 13.sp, color = Color.Gray, maxLines = 1)
        }
    }
}

@Composable
fun ActionContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(WhatsappTealGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
    }
}
