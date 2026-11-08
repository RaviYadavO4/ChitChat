package com.portfolio.chitchat.ui.settings.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portfolio.chitchat.ui.settings.SettingsOptionRow
import com.portfolio.chitchat.ui.theme.WhatsappTealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(onBackClicked: () -> Unit) {
    // Structural layout state switches for live configuration boxes
    var showDeleteWizard by remember { mutableStateOf(false) }
    var showPrivacyNotice by remember { mutableStateOf(false) }
    var showSecurityDetails by remember { mutableStateOf(false) }
    var showPasskeyStatus by remember { mutableStateOf(false) }
    var showNumberMigration by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Account", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsOptionRow(
                icon = Icons.Filled.PrivacyTip,
                title = "Privacy",
                subtitle = "Block contacts, disappearing messages",
                onClick = { showPrivacyNotice = true }
            )
            SettingsOptionRow(
                icon = Icons.Filled.Security,
                title = "Security notifications",
                subtitle = "Show notifications when code changes",
                onClick = { showSecurityDetails = true }
            )
            SettingsOptionRow(
                icon = Icons.Filled.Key,
                title = "Passkeys",
                subtitle = "Secure methods to log in safely",
                onClick = { showPasskeyStatus = true }
            )
            SettingsOptionRow(
                icon = Icons.Filled.PhonelinkSetup,
                title = "Change number",
                subtitle = "Migrate your account info & groups",
                onClick = { showNumberMigration = true }
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))

            SettingsOptionRow(
                icon = Icons.Filled.Delete,
                title = "Delete account",
                subtitle = "Wipe all message logs & group memberships",
                onClick = { showDeleteWizard = true }
            )
        }

        // 1. WhatsApp Style Account Destruction Dialog
        if (showDeleteWizard) {
            AlertDialog(
                onDismissRequest = { showDeleteWizard = false },
                title = { Text(text = "Delete this account?", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = { Text("Deleting your account will erase your message history, delete you from all groups, and reset account sync logs permanently.", color = MaterialTheme.colorScheme.onBackground) },
                confirmButton = {
                    TextButton(onClick = { showDeleteWizard = false }) {
                        Text("DELETE", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteWizard = false }) {
                        Text("CANCEL", color = WhatsappTealGreen, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // 2. Functional Privacy Interactive Sheet Dialog
        if (showPrivacyNotice) {
            AlertDialog(
                onDismissRequest = { showPrivacyNotice = false },
                title = { Text(text = "Privacy Config", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = { Text("End-to-End encrypted parameters are active. Blocked contacts and active profile read status filters can be edited here after backend integration.", color = MaterialTheme.colorScheme.onBackground) },
                confirmButton = {
                    TextButton(onClick = { showPrivacyNotice = false }) { Text("OK", color = WhatsappTealGreen, fontWeight = FontWeight.Bold) }
                }
            )
        }

        // 3. Security Notifications Informative Component Box
        if (showSecurityDetails) {
            AlertDialog(
                onDismissRequest = { showSecurityDetails = false },
                title = { Text(text = "Security Logs", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = { Text("ChitChat protects your text messages and voice/video channels with secure cryptographic algorithms. Security alerts are synchronized.", color = MaterialTheme.colorScheme.onBackground) },
                confirmButton = {
                    TextButton(onClick = { showSecurityDetails = false }) { Text("CLOSE", color = WhatsappTealGreen, fontWeight = FontWeight.Bold) }
                }
            )
        }

        // 4. Passkey Registration Dashboard
        if (showPasskeyStatus) {
            AlertDialog(
                onDismissRequest = { showPasskeyStatus = false },
                title = { Text(text = "Passkey Setup", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = { Text("Your device biometrics (Fingerprint/Face ID) can be registered as passkeys to securely authenticating Single-Device session rules.", color = MaterialTheme.colorScheme.onBackground) },
                confirmButton = {
                    TextButton(onClick = { showPasskeyStatus = false }) { Text("INITIALIZE", color = WhatsappTealGreen, fontWeight = FontWeight.Bold) }
                }
            )
        }

        // 5. Change Number Migration Console Overlay
        if (showNumberMigration) {
            AlertDialog(
                onDismissRequest = { showNumberMigration = false },
                title = { Text(text = "Migrate Phone Number", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = { Text("Using this wizard will safely transfer your active identity data, chat layouts, and settings credentials from your old number to a new one.", color = MaterialTheme.colorScheme.onBackground) },
                confirmButton = {
                    TextButton(onClick = { showNumberMigration = false }) { Text("PROCEED", color = WhatsappTealGreen, fontWeight = FontWeight.Bold) }
                }
            )
        }
    }
}
