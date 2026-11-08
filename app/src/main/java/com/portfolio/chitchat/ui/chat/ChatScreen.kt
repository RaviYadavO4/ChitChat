package com.portfolio.chitchat.ui.chat

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.data.model.Message
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen
import com.portfolio.chitchat.utils.FileUtils
import com.portfolio.chitchat.utils.shimmerEffect
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    userName: String,
    receiverUid: String,
    viewModel: ChatViewModel,
    onBackClicked: () -> Unit,
    onNavigateToImagePreview: () -> Unit,
    onNavigateToPdfPreview: () -> Unit
) {
    val liveMessages by viewModel.conversationMessages.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()
    val context = LocalContext.current

    // Premium Interaction Menu States System (Flipkart Standard Interface Mapping)
    var selectedMessageForAction by remember { mutableStateOf<Message?>(null) }
    var showActionBottomSheet by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var editTextFieldState by remember { mutableStateOf("") }

    LaunchedEffect(key1 = receiverUid) {
        viewModel.loadAndWatchMessages(receiverUid)
    }

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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Navigation", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Filled.VideoCall, contentDescription = "Video Call", tint = Color.White) }
                    IconButton(onClick = { }) { Icon(Icons.Filled.Call, contentDescription = "Voice Call", tint = Color.White) }
                    IconButton(onClick = { }) { Icon(Icons.Filled.MoreVert, contentDescription = "More Menu Options", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsappTealGreen)
            )
        },
        bottomBar = {
            ChatInputBar(onSendMessageDispatched = { msgText, msgType, base64Payload ->
                viewModel.dispatchMessage(receiverUid = receiverUid, text = msgText, type = msgType, base64Payload = base64Payload)
            })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFECE5DD))
        ) {
            if (isChatLoading) {
                // 1. High-Fidelity Alternating Shimmer Skeleton Framework
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(6) { index ->
                        val alignmentSetting = if (index % 2 == 0) Alignment.CenterEnd else Alignment.CenterStart
                        val widthPercentage = if (index % 3 == 0) 0.65f else if (index % 2 == 0) 0.45f else 0.55f
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = alignmentSetting
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(widthPercentage)
                                    .height(45.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .shimmerEffect()
                            )
                        }
                    }
                }
            } else {
                // 2. Real-Time Conversation Stream Active Layout Matrix
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(liveMessages, key = { it.messageId }) { fbMessage ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        if (fbMessage.type == "image") {
                                            viewModel.cachePreviewPayload(fbMessage.fileBase64)
                                            onNavigateToImagePreview()
                                        } else if (fbMessage.type == "pdf") {
                                            FileUtils.openBase64PdfDocument(context, fbMessage.fileBase64)
                                        }
                                    },
                                    onLongClick = {
                                        if (!fbMessage.text.contains("This message was deleted")) {
                                            selectedMessageForAction = fbMessage
                                            editTextFieldState = fbMessage.text
                                            showActionBottomSheet = true
                                        }
                                    }
                                )
                        ) {
                            // Corrected Callout: Clashing high-order parameters block isolated to Box wrapper layer
                            MessageBubble(message = fbMessage)
                        }
                    }
                }
            }

            // 3. Contextual Options Sheets Framework (Flipkart Vibe Matrix Rules)
            if (showActionBottomSheet && selectedMessageForAction != null) {
                ModalBottomSheet(
                    onDismissRequest = { showActionBottomSheet = false },
                    containerColor = Color.White
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                        Text(
                            text = "Message Options",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = WhatsappTealGreen,
                            modifier = Modifier.padding(16.dp)
                        )

                        val currentAuthUid = remember { com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: "" }
                        val isMyOwnMessage = selectedMessageForAction!!.senderId == currentAuthUid

                        // Edit functionality reserved explicitly for outgoing text elements
                        if (isMyOwnMessage && selectedMessageForAction!!.type == "text") {
                            ListItem(
                                headlineContent = { Text("Edit Message", color = Color.Black) },
                                leadingContent = { Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.Gray) },
                                modifier = Modifier.clickable { showActionBottomSheet = false; showEditDialog = true }
                            )
                        }

                        // Intelligent Double-State Selection Filters
                        if (isMyOwnMessage) {
                            ListItem(
                                headlineContent = { Text("Delete for Everyone", color = Color.Red) },
                                leadingContent = { Icon(Icons.Filled.Delete, contentDescription = "Delete Everyone", tint = Color.Red) },
                                modifier = Modifier.clickable {
                                    showActionBottomSheet = false
                                    viewModel.requestDeleteForEveryone(receiverUid, selectedMessageForAction!!.messageId)
                                }
                            )
                        }

                        ListItem(
                            headlineContent = { Text("Delete for Me", color = Color.Black) },
                            leadingContent = { Icon(Icons.Filled.Delete, contentDescription = "Delete Local Me", tint = Color.Gray) },
                            modifier = Modifier.clickable {
                                showActionBottomSheet = false
                                viewModel.requestDeleteForMe(receiverUid, selectedMessageForAction!!.messageId)
                            }
                        )
                        ListItem(
                            headlineContent = { Text("Message Info", color = Color.Black) },
                            leadingContent = { Icon(Icons.Filled.Info, contentDescription = "Info", tint = Color.Gray) },
                            modifier = Modifier.clickable { showActionBottomSheet = false; showInfoDialog = true }
                        )
                    }
                }
            }
// 4. Text Modification Dialog Widget
            if (showEditDialog && selectedMessageForAction != null) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text("Edit Message text", fontWeight = FontWeight.Bold) },
                    text = {
                        OutlinedTextField(
                            value = editTextFieldState,
                            onValueChange = { editTextFieldState = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = WhatsappTealGreen)
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (editTextFieldState.isNotBlank()) {
                                viewModel.requestMessageEdit(receiverUid, selectedMessageForAction!!.messageId, editTextFieldState)
                                showEditDialog = false
                            }
                        }) { Text("UPDATE", color = WhatsappTealGreen, fontWeight = FontWeight.Bold) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEditDialog = false }) { Text("CANCEL", color = Color.Gray) }
                    }
                )
            }
// 5. Detailed Tracking Metrices Timestamp Panel (Message Info)
            if (showInfoDialog && selectedMessageForAction != null) {
                val formattedTime = remember(selectedMessageForAction!!.timestamp) {
                    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    sdf.format(Date(selectedMessageForAction!!.timestamp))
                }
                AlertDialog(
                    onDismissRequest = { showInfoDialog = false },
                    title = { Text("Message Info", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text(text = "Ticks Status: ${selectedMessageForAction!!.status.replaceFirstChar { it.uppercase() }}", fontSize = 16.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Timestamp: $formattedTime", fontSize = 14.sp, color = Color.Gray)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showInfoDialog = false }) { Text("OK", color = WhatsappTealGreen, fontWeight = FontWeight.Bold) }
                    }
                )
            }
        }
    }
}