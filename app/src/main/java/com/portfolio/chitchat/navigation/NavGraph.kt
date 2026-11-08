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
import com.portfolio.chitchat.ui.chat.ChatViewModel
import com.portfolio.chitchat.ui.chat.ImagePreviewScreen
import com.portfolio.chitchat.ui.chat.PdfPreviewScreen
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
    val authViewModel: AuthViewModel = viewModel()

    // Shared Chat ViewModel scope protecting large payload streams from route overflow crashes
    val sharedChatViewModel: ChatViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. Splash Screen with Persistent Active Session Route Checker Logic
        composable(route = Screen.Splash.route) {
            SplashScreen()
            LaunchedEffect(key1 = true) {
                delay(3000L)
                val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        // 2. Phone Login Screen
        composable(route = Screen.Login.route) {
            LoginScreen(viewModel = authViewModel, onOtpSentNav = { phone -> navController.navigate(Screen.Otp.passPhone(phone)) })
        }

        // 3. OTP Screen
        composable(route = Screen.Otp.route, arguments = listOf(navArgument("phone") { type = NavType.StringType })) { entry ->
            val phone = entry.arguments?.getString("phone") ?: ""
            OtpScreen(phoneNumber = phone, viewModel = authViewModel, onVerifiedNav = { navController.navigate(Screen.ProfileSetup.route) })
        }

        // 4. User Profile Creation Setup
        composable(route = Screen.ProfileSetup.route) {
            ProfileSetupScreen(viewModel = authViewModel, onProfileFinishedNav = {
                navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } }
            })
        }

        // 5. Main Dashboard View
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onChatClicked = { resolvedName, resolvedUid ->
                    navController.navigate(Screen.Chat.passNameAndUid(resolvedName, resolvedUid))
                },
                onStatusClicked = { name -> navController.navigate(Screen.StatusViewer.passName(name)) },
                onCallClicked = { name -> navController.navigate(Screen.VoiceCall.passName(name)) },
                onFabClicked = { navController.navigate(Screen.SelectContact.route) },
                onSettingsClicked = { navController.navigate(Screen.Settings.route) }
            )
        }

        // 6. One-to-One Conversation Layout Node with high fidelity preview route handlers
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "User"
            val uid = backStackEntry.arguments?.getString("uid") ?: ""

            ChatScreen(
                userName = name,
                receiverUid = uid,
                viewModel = sharedChatViewModel,
                onBackClicked = { navController.popBackStack() },
                onNavigateToImagePreview = { navController.navigate(Screen.ImagePreview.route) },
                onNavigateToPdfPreview = { navController.navigate(Screen.PdfPreview.route) }
            )
        }

        // 7. Full-Screen Photo File View Layer Overlay Canvas Destination
        composable(route = Screen.ImagePreview.route) {
            ImagePreviewScreen(viewModel = sharedChatViewModel, onBackClicked = { navController.popBackStack() })
        }

        // 8. Document PDF File Viewer Canvas Destination
        composable(route = Screen.PdfPreview.route) {
            PdfPreviewScreen(viewModel = sharedChatViewModel, onBackClicked = { navController.popBackStack() })
        }

        // Remaining Voice, Video Calling logs overlay nodes remain exact same as safe layout mapping structure
        composable(route = Screen.VoiceCall.route, arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry -> VoiceCallScreen(userName = entry.arguments?.getString("name") ?: "User", onEndCallClicked = { navController.popBackStack() }) }
        composable(route = Screen.VideoCall.route, arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry -> VideoCallScreen(userName = entry.arguments?.getString("name") ?: "User", onEndCallClicked = { navController.popBackStack() }) }
        composable(route = Screen.StatusViewer.route, arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry -> StatusViewerScreen(userName = entry.arguments?.getString("name") ?: "User", onStoryTimedOut = { navController.popBackStack() }) }
        composable(route = Screen.SelectContact.route) { SelectContactScreen(onBackClicked = { navController.popBackStack() }, onCreateGroupClicked = { navController.navigate(Screen.CreateGroup.route) }, onContactSelected = { name, uid -> navController.navigate(Screen.Chat.passNameAndUid(name, uid)) { popUpTo(Screen.Dashboard.route) } }) }
        composable(route = Screen.CreateGroup.route) { CreateGroupScreen(onBackClicked = { navController.popBackStack() }, onDoneClicked = { navController.popBackStack() }) }
        composable(route = Screen.Settings.route) { SettingsScreen(onBackClicked = { navController.popBackStack() }, onAccountClicked = { navController.navigate(Screen.AccountSettings.route) }, onChatsClicked = { navController.navigate(Screen.ChatsSettings.route) }, onNotificationsClicked = { navController.navigate(Screen.NotificationsSettings.route) }) }
        composable(route = Screen.AccountSettings.route) { AccountSettingsScreen(onBackClicked = { navController.popBackStack() }) }
        composable(route = Screen.ChatsSettings.route) { ChatsSettingsScreen(onBackClicked = { navController.popBackStack() }, isDarkMode = isDarkMode, onThemeChanged = onThemeChanged) }
        composable(route = Screen.NotificationsSettings.route) { NotificationsSettingsScreen(onBackClicked = { navController.popBackStack() }) }
    }
}
