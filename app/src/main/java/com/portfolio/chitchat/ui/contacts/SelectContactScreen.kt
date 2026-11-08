package com.portfolio.chitchat.ui.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

data class DummyContact(val id: String, val name: String, val status: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectContactScreen(
    onBackClicked: () -> Unit,
    onCreateGroupClicked: () -> Unit,
    onContactSelected: (String) -> Unit // Navigates directly to the dynamic chat screen
) {
    val dummyContacts = remember {
        listOf(
            DummyContact("1", "Aman Verma", "Hey there! I am using ChitChat."),
            DummyContact("2", "Bhavna Singh", "Busy at work. Messages only."),
            DummyContact("3", "Rahul Sharma", "Available"),
            DummyContact("4", "Vikram Singh", "Coding all night 🚀"),
            DummyContact("5", "Deepak Kumar", "At the gym 🏋️‍♂️"),
            DummyContact("6", "Neha Gupta", "Can't talk, ChitChat only.")
        )
    }

    // State management for local real-time search engine
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Multi-criteria filter matching user input query dynamically
    val filteredContacts = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            dummyContacts
        } else {
            dummyContacts.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.status.contains(searchQuery, ignoreCase = true)
            }
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
                            placeholder = { Text("Search contacts...", color = Color.White.copy(alpha = 0.6f), fontSize = 16.sp) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column {
                            Text(text = "Select contact", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "${dummyContacts.size} contacts", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isSearchActive) {
                            isSearchActive = false
                            searchQuery = ""
                        } else {
                            onBackClicked()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Navigation", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isSearchActive) {
                            searchQuery = ""
                        } else {
                            isSearchActive = true
                        }
                    }) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Filled.Close else Icons.Filled.Search,
                            contentDescription = "Search Trigger Action Toggle",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Action headers hide automatically when filtering to prioritize matches
            if (!isSearchActive || searchQuery.isBlank()) {
                item {
                    ActionContactRow(icon = Icons.Filled.GroupAdd, title = "New group", onClick = onCreateGroupClicked)
                    ActionContactRow(icon = Icons.Filled.PersonAdd, title = "New contact", onClick = { })
                    Text(
                        text = "Contacts on ChitChat",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }

            if (filteredContacts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No contacts match your search.", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            } else {
                items(filteredContacts) { contact ->
                    Surface(
                        onClick = { onContactSelected(contact.name) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ContactListItemRow(contact)
                    }
                }
            }
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

@Composable
fun ContactListItemRow(contact: DummyContact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = contact.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = contact.status, fontSize = 13.sp, color = Color.Gray, maxLines = 1)
        }
    }
}
