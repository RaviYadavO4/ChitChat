package com.portfolio.chitchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.portfolio.chitchat.navigation.SetupNavGraph
import com.portfolio.chitchat.ui.theme.ChitChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            // Check if user session exists persistently under the hood
            val currentUser = remember { FirebaseAuth.getInstance().currentUser }
            val startDestination = if (currentUser != null) "dashboard_screen" else "splash_screen"

            ChitChatTheme(isDarkMode = isDarkMode) {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onThemeChanged = { dark -> isDarkMode = dark }
                    // Update: SetupNavGraph method internally handles this start destination string parameter
                )
            }
        }
    }
}
