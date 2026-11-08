package com.portfolio.chitchat.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(phoneNumber: String, onVerifyClicked: (String) -> Unit) {
    var otpCode by remember { mutableStateOf("") }
    var timerSeconds by remember { mutableStateOf(59) }

    // Dynamic countdown timer for active verification ticks
    LaunchedEffect(key1 = timerSeconds) {
        if (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Verifying your number",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = WhatsappTealGreen
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Waiting to automatically detect an SMS sent to\n$phoneNumber.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))

        // BasicTextField handling core logic for input bounds
        BasicTextField(
            value = otpCode,
            onValueChange = {
                if (it.length <= 6) {
                    otpCode = it
                    if (it.length == 6) {
                        onVerifyClicked(it)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                Box {
                    // Hidden field handles state updates under the hood
                    innerTextField()

                    // Visual display container for the 6 boxes
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until 6) {
                            val char = when {
                                i < otpCode.length -> otpCode[i].toString()
                                else -> ""
                            }
                            val isFocused = i == otpCode.length

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(55.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF7F7F7)) // Correct Material3 hex format mapping
                                    .border(
                                        width = if (isFocused) 2.dp else 1.dp,
                                        color = if (isFocused) WhatsappTealGreen else Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Timer feedback logic layout blocks
        if (timerSeconds > 0) {
            Text(
                text = "Resend SMS in 0:${timerSeconds.toString().padStart(2, '0')}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        } else {
            TextButton(onClick = { timerSeconds = 59 }) {
                Text(text = "Resend SMS", color = WhatsappTealGreen, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { if (otpCode.length == 6) onVerifyClicked(otpCode) },
            colors = ButtonDefaults.buttonColors(containerColor = WhatsappTealGreen),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(text = "VERIFY", color = Color.White, modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}
