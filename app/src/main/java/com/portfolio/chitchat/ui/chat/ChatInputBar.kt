package com.portfolio.chitchat.ui.chat

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen
import com.portfolio.chitchat.utils.AudioRecorder
import com.portfolio.chitchat.utils.FileUtils
import com.portfolio.chitchat.utils.ImageUtils

@Composable
fun ChatInputBar(onSendMessageDispatched: (String, String, String) -> Unit) {
    var textState by remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val audioRecorder = remember { AudioRecorder(context) }
    var isRecording by remember { mutableStateOf(false) }
    var showAttachmentMenu by remember { mutableStateOf(false) }

    // Intent launchers mapping base64 payload converters directly
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val base64Str = ImageUtils.uriToBase64(context, it)
            if (base64Str != null) onSendMessageDispatched("", "image", base64Str)
        }
    }

    val docLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val base64Str = FileUtils.fileToBase64(context, it)
            if (base64Str == "OVERSIZE_ERROR") {
                Toast.makeText(context, "File too large! Max limit 800KB", Toast.LENGTH_LONG).show()
            } else if (base64Str != null) {
                onSendMessageDispatched("📄 Document Shared", "pdf", base64Str)
            }
        }
    }

    // Native Permission Request Pipeline Hook
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
        if (audioGranted) {
            Toast.makeText(context, "Permissions Granted! Tap again to perform action.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Hardware permissions are mandatory for media transfer.", Toast.LENGTH_SHORT).show()
        }
    }

    // Context execution logic utility helper to verify permission pools
    val hasHardwareGrants = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (showAttachmentMenu) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        showAttachmentMenu = false
                        docLauncher.launch("application/pdf")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                ) { Text("Document", color = Color.White) }

                Button(
                    onClick = {
                        showAttachmentMenu = false
                        imageLauncher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) { Text("Gallery", color = Color.White) }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f).background(Color.White, shape = RoundedCornerShape(24.dp)).padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    keyboardController?.show()
                    Toast.makeText(context, "Use soft keyboard panel 😊", Toast.LENGTH_SHORT).show()
                }) { Icon(Icons.Filled.Mood, contentDescription = "Emoji", tint = Color.Gray) }

                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    placeholder = { Text(text = if(isRecording) "Recording Voice Note..." else "Message", color = Color.Gray, fontSize = 16.sp) },
                    modifier = Modifier.weight(1f),
                    enabled = !isRecording,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent, disabledIndicatorColor = Color.Transparent
                    )
                )

                IconButton(onClick = { showAttachmentMenu = !showAttachmentMenu }) {
                    Icon(Icons.Filled.AttachFile, contentDescription = "Attach", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(if(isRecording) Color.Red else WhatsappTealGreen),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        if (textState.isNotBlank()) {
                            onSendMessageDispatched(textState, "text", "")
                            textState = ""
                        } else {
                            // Direct verification mapping: If grants exist, launch engines. Otherwise launch native popups
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                if (!isRecording) {
                                    val recordingPath = audioRecorder.startRecording()
                                    if (recordingPath != null) {
                                        isRecording = true
                                        Toast.makeText(context, "Recording started...", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    val recordedFile = audioRecorder.stopRecording()
                                    isRecording = false
                                    if (recordedFile != null && recordedFile.exists()) {
                                        val audioBytes = recordedFile.readBytes()
                                        val base64Audio = android.util.Base64.encodeToString(audioBytes, android.util.Base64.DEFAULT)
                                        onSendMessageDispatched("🎙️ Voice Note", "audio", base64Audio)
                                    }
                                }
                            } else {
                                // Dynamic triggers invoking original Android runtime OS popups hooks directly
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                )
                            }
                        }
                    }
                ) {
                    Icon(imageVector = if (textState.isNotBlank()) Icons.Filled.Send else Icons.Filled.Mic, contentDescription = "Action", tint = Color.White)
                }
            }
        }
    }
}
