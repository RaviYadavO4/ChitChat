package com.portfolio.chitchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.portfolio.chitchat.navigation.SetupNavGraph
import com.portfolio.chitchat.ui.theme.ChitChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Global runtime theme state tracker node
            var isDarkMode by remember { mutableStateOf(false) }

            ChitChatTheme(isDarkMode = isDarkMode) {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onThemeChanged = { dark -> isDarkMode = dark }
                )
            }
        }
    }
}
