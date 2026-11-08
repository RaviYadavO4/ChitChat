package com.portfolio.chitchat.ui.auth

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@Composable
fun ProfileSetupScreen(onFinishClicked: (String) -> Unit) {
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Profile Info",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = WhatsappTealGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Please provide your name and an optional profile photo",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Circular Profile Image Placeholder with Camera Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.PhotoCamera,
                contentDescription = "Select Photo",
                modifier = Modifier.size(36.dp),
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Name Input Field
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            placeholder = { Text("Type your name here") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WhatsappTealGreen,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { if (userName.isNotBlank()) onFinishClicked(userName) },
            colors = ButtonDefaults.buttonColors(containerColor = WhatsappTealGreen),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(text = "FINISH", color = Color.White, modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}
