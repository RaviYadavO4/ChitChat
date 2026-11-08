package com.portfolio.chitchat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.portfolio.chitchat.ui.auth.AuthViewModel
import com.portfolio.chitchat.ui.auth.LoginScreen
import com.portfolio.chitchat.ui.auth.OtpScreen
import com.portfolio.chitchat.ui.auth.ProfileSetupScreen
import com.portfolio.chitchat.ui.dashboard.DashboardScreen
import com.portfolio.chitchat.ui.splash.SplashScreen
import com.portfolio.chitchat.ui.chat.ChatScreen
import com.portfolio.chitchat.ui.calling.VoiceCallScreen
import com.portfolio.chitchat.ui.calling.VideoCallScreen
import com.portfolio.chitchat.ui.status.StatusViewerScreen
import com.portfolio.chitchat.ui.contacts.SelectContactScreen
import com.portfolio.chitchat.ui.contacts.CreateGroupScreen
import com.portfolio.chitchat.ui.settings.SettingsScreen
import com.portfolio.chitchat.ui.settings.sub.AccountSettingsScreen
import com.portfolio.chitchat.ui.settings.sub.ChatsSettingsScreen
import com.portfolio.chitchat.ui.settings.sub.NotificationsSettingsScreen
import kotlinx.coroutines.delay

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    // Single shared instance of AuthViewModel spanning authentication sequence lifecycle
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. Splash Screen (3 Seconds Dynamic Auto-Timer to Login)
        composable(route = Screen.Splash.route) {
            SplashScreen()
            LaunchedEffect(key1 = true) {
                delay(3000L)
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }

        // 2. Mobile Phone Authentication Login Screen with ViewModel Integration
        composable(route = Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onOtpSentNav = { fullPhone ->
                    navController.navigate(Screen.Otp.passPhone(fullPhone))
                }
            )
        }

        // 3. Dynamic OTP Verification Screen with Shared ViewModel Integration
        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreen(
                phoneNumber = phone,
                viewModel = authViewModel,
                onVerifiedNav = {
                    navController.navigate(Screen.ProfileSetup.route)
                }
            )
        }

        // 4. Initial User Profile Setup Screen with Shared ViewModel Integration
        composable(route = Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                viewModel = authViewModel,
                onProfileFinishedNav = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 5. Main Dashboard Engine View
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onChatClicked = { name -> navController.navigate(Screen.Chat.passName(name)) },
                onStatusClicked = { name -> navController.navigate(Screen.StatusViewer.passName(name)) },
                onCallClicked = { name -> navController.navigate(Screen.VoiceCall.passName(name)) },
                onFabClicked = { navController.navigate(Screen.SelectContact.route) },
                onSettingsClicked = { navController.navigate(Screen.Settings.route) }
            )
        }

        // 6. One-to-One Classic Message Conversation Window View
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "User"
            ChatScreen(userName = name, onBackClicked = { navController.popBackStack() })
        }

        // 7. Voice Call Layout Interface Canvas
        composable(
            route = Screen.VoiceCall.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "User"
            VoiceCallScreen(userName = name, onEndCallClicked = { navController.popBackStack() })
        }

        // 8. Video Call Preview Layout Frame Simulator
        composable(
            route = Screen.VideoCall.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "User"
            VideoCallScreen(userName = name, onEndCallClicked = { navController.popBackStack() })
        }

        // 9. Status Story Time-Bounded Segment Viewer View
        composable(
            route = Screen.StatusViewer.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "User"
            StatusViewerScreen(userName = name, onStoryTimedOut = { navController.popBackStack() })
        }

        // 10. Select Contact Screen
        composable(route = Screen.SelectContact.route) {
            SelectContactScreen(
                onBackClicked = { navController.popBackStack() },
                onCreateGroupClicked = { navController.navigate(Screen.CreateGroup.route) },
                onContactSelected = { selectedName ->
                    navController.navigate(Screen.Chat.passName(selectedName)) {
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        // 11. Multi-Participant Group Creation Filter Panels Component
        composable(route = Screen.CreateGroup.route) {
            CreateGroupScreen(
                onBackClicked = { navController.popBackStack() },
                onDoneClicked = { navController.popBackStack() }
            )
        }

        // 12. App Settings Configuration List View
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClicked = { navController.popBackStack() },
                onAccountClicked = { navController.navigate(Screen.AccountSettings.route) },
                onChatsClicked = { navController.navigate(Screen.ChatsSettings.route) },
                onNotificationsClicked = { navController.navigate(Screen.NotificationsSettings.route) }
            )
        }

        // 13. Account sub-settings routing
        composable(route = Screen.AccountSettings.route) {
            AccountSettingsScreen(onBackClicked = { navController.popBackStack() })
        }

        // 14. Chats sub-settings routing
        composable(route = Screen.ChatsSettings.route) {
            ChatsSettingsScreen(
                onBackClicked = { navController.popBackStack() },
                isDarkMode = isDarkMode,
                onThemeChanged = onThemeChanged
            )
        }

        // 15. Notifications sub-settings routing
        composable(route = Screen.NotificationsSettings.route) {
            NotificationsSettingsScreen(onBackClicked = { navController.popBackStack() })
        }
    }
}
