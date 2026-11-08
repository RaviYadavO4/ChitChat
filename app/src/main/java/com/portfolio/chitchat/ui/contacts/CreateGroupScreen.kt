package com.portfolio.chitchat.ui.contacts

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.data.model.User
import com.portfolio.chitchat.data.repository.ContactRepository
import com.portfolio.chitchat.data.repository.GroupRepository
import com.portfolio.chitchat.ui.theme.WhatsappLightGreen
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen
import com.portfolio.chitchat.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    onBackClicked: () -> Unit,
    onDoneClicked: () -> Unit
) {
    val context = LocalContext.current
    val contactRepo = remember { ContactRepository() }
    val groupRepo = remember { GroupRepository() }

    var contactsList by remember { mutableStateOf<List<User>>(emptyList()) }
    val selectedContacts = remember { mutableStateListOf<User>() }
    var isLoading by remember { mutableStateOf(true) }
    var isCreatingGroup by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var showGroupNameDialog by remember { mutableStateOf(false) }
    var groupNameInput by remember { mutableStateOf("") }

    // Fetch contacts from Firestore
    LaunchedEffect(Unit) {
        contactRepo.fetchAllChitChatContacts { fetched ->
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
                    Column {
                        Text(text = "New group", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            text = if (selectedContacts.isEmpty()) "Add participants" else "${selectedContacts.size} of ${contactsList.size} selected",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
            )
        },
        floatingActionButton = {
            if (selectedContacts.isNotEmpty() && !isCreatingGroup) {
                FloatingActionButton(
                    onClick = { showGroupNameDialog = true },
                    containerColor = WhatsappLightGreen,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.ArrowForward, contentDescription = "Proceed to Group Name")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = WhatsappTealGreen
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search contacts...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WhatsappTealGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = CircleShape
                    )

                    // Horizontal Selected Chips Row
                    if (selectedContacts.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedContacts) { user ->
                                SelectedUserChip(user = user, onRemove = { selectedContacts.remove(user) })
                            }
                        }
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 1.dp)
                    }

                    // Contacts List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(filteredContacts) { user ->
                            val isChecked = selectedContacts.contains(user)
                            Surface(
                                onClick = {
                                    if (isChecked) selectedContacts.remove(user) else selectedContacts.add(user)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (user.profileImageBase64.isNotBlank()) {
                                        val bmp = remember(user.profileImageBase64) { ImageUtils.base64ToBitmap(user.profileImageBase64) }
                                        bmp?.let {
                                            Image(
                                                bitmap = it.asImageBitmap(),
                                                contentDescription = "Avatar",
                                                modifier = Modifier.size(48.dp).clip(CircleShape),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.LightGray),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Filled.Group, contentDescription = "Default", tint = Color.DarkGray)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        Text(text = user.statusText, fontSize = 13.sp, color = Color.Gray, maxLines = 1)
                                    }
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            if (checked) selectedContacts.add(user) else selectedContacts.remove(user)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = WhatsappLightGreen)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Group Name Pro Dialog Modal
            if (showGroupNameDialog) {
                AlertDialog(
                    onDismissRequest = { if (!isCreatingGroup) showGroupNameDialog = false },
                    title = { Text(text = "Provide Group Subject", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Enter a name for your new group and add participants.", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = groupNameInput,
                                onValueChange = { groupNameInput = it },
                                placeholder = { Text("Group Name (e.g. Android Devs)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = WhatsappTealGreen)
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (groupNameInput.isNotBlank()) {
                                    isCreatingGroup = true
                                    val memberUids = selectedContacts.map { it.uid }
                                    groupRepo.createGroupChannel(groupNameInput.trim(), memberUids) { success ->
                                        isCreatingGroup = false
                                        showGroupNameDialog = false
                                        if (success) {
                                            Toast.makeText(context, "Group Created Successfully!", Toast.LENGTH_SHORT).show()
                                            onDoneClicked()
                                        } else {
                                            Toast.makeText(context, "Failed to create group. Try again.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Please enter a group name", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = WhatsappTealGreen),
                            enabled = !isCreatingGroup
                        ) {
                            if (isCreatingGroup) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text("CREATE")
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showGroupNameDialog = false },
                            enabled = !isCreatingGroup
                        ) {
                            Text("Cancel", color = Color.Gray)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SelectedUserChip(user: User, onRemove: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = WhatsappTealGreen.copy(alpha = 0.15f),
        modifier = Modifier.height(34.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = user.name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = WhatsappTealGreen)
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onRemove, modifier = Modifier.size(18.dp)) {
                Icon(Icons.Filled.Close, contentDescription = "Remove", tint = WhatsappTealGreen, modifier = Modifier.size(14.dp))
            }
        }
    }
}
