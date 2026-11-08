package com.portfolio.chitchat.ui.contacts

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappLightGreen
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(onBackClicked: () -> Unit, onDoneClicked: () -> Unit) {
    val sampleContacts = remember {
        mutableStateListOf(
            DummyContact("1", "Aman Verma", "Online"),
            DummyContact("2", "Bhavna Singh", "Away"),
            DummyContact("3", "Rahul Sharma", "Available"),
            DummyContact("4", "Vikram Singh", "Coding...")
        )
    }
    val selectedContacts = remember { mutableStateListOf<DummyContact>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "New group", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Add participants", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
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
            if (selectedContacts.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onDoneClicked,
                    containerColor = WhatsappLightGreen,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.ArrowForward, contentDescription = "Proceed to name group")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Horizontal Top Row Selected List Chips Layout
            if (selectedContacts.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedContacts) { contact ->
                        SelectedUserChip(contact = contact, onRemove = { selectedContacts.remove(contact) })
                    }
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
            }

            // Main Selection Body List Component Nodes
            LazyColumn(modifier = Modifier.fillWeight(1f)) {
                items(sampleContacts) { contact ->
                    val isChecked = selectedContacts.contains(contact)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = contact.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                            color = Color.Black
                        )
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                if (checked) selectedContacts.add(contact) else selectedContacts.remove(contact)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = WhatsappLightGreen)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedUserChip(contact: DummyContact, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.4f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = contact.name, fontSize = 13.sp, color = Color.Black)
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onRemove, modifier = Modifier.size(16.dp)) {
                Icon(Icons.Filled.Close, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(12.dp))
            }
        }
    }
}

// Custom internal utility extension wrapper parameter fix matching compose scope
private fun Modifier.fillWeight(weight: Float): Modifier = this.then(Modifier.fillMaxWidth().fillMaxHeight(weight))
