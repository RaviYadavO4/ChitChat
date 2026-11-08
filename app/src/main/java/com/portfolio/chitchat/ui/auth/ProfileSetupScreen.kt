package com.portfolio.chitchat.ui.auth

import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen
import com.portfolio.chitchat.utils.ImageUtils

@Composable
fun ProfileSetupScreen(
    viewModel: AuthViewModel,
    onProfileFinishedNav: () -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var imageBase64String by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    // Gallery Picker Engine capturing stream vectors
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val converted = ImageUtils.uriToBase64(context, it)
            if (converted != null) {
                imageBase64String = converted
                Toast.makeText(context, "Image Processed Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Image conversion failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.ProfileCreated && (authState as AuthState.ProfileCreated).success) {
            onProfileFinishedNav()
        } else if (authState is AuthState.Error) {
            Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "Profile Info", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = WhatsappTealGreen)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Please provide your name and an optional profile photo", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.6f))
                .clickable { galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Filled.PhotoCamera, contentDescription = "Select Photo", modifier = Modifier.size(36.dp), tint = Color.Gray)
        }

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            placeholder = { Text("Type your name here") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = WhatsappTealGreen, unfocusedBorderColor = Color.Gray)
        )

        Spacer(modifier = Modifier.weight(1f))

        if (authState is AuthState.Loading) {
            CircularProgressIndicator(color = WhatsappTealGreen, modifier = Modifier.padding(bottom = 24.dp))
        } else {
            Button(
                onClick = {
                    if (userName.isNotBlank()) {
                        // Unique hardware profile identifier fetch to ensure Single-Device constraint matrix rules
                        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                        viewModel.saveProfile(userName, imageBase64String, deviceId)
                    } else {
                        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = WhatsappTealGreen),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(text = "FINISH", color = Color.White, modifier = Modifier.padding(horizontal = 24.dp))
            }
        }
    }
}
