package com.portfolio.chitchat.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Center Logo Container
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Send, // WhatsApp style placeholder logo
                contentDescription = "App Logo",
                modifier = Modifier.size(90.dp),
                tint = WhatsappTealGreen
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ChitChat",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsappTealGreen
            )
        }

        // Bottom Studio Branding
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "from",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "PORTFOLIO STUDIO",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsappTealGreen,
                letterSpacing = 2.sp
            )
        }
    }
}
