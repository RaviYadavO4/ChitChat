package com.portfolio.chitchat.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@Composable
fun LoginScreen(onNextClicked: (String) -> Unit) {
    var countryCode by remember { mutableStateOf("+91") }
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Verify your phone number",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = WhatsappTealGreen
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ChitChat will send an SMS message to verify your phone number.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))

        // Phone Input fields
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = countryCode,
                onValueChange = { countryCode = it },
                modifier = Modifier.width(70.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhatsappTealGreen
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 10) phoneNumber = it },
                placeholder = { Text("phone number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhatsappTealGreen
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { if (phoneNumber.length == 10) onNextClicked("$countryCode$phoneNumber") },
            colors = ButtonDefaults.buttonColors(containerColor = WhatsappTealGreen),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(text = "NEXT", color = Color.White, modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}
