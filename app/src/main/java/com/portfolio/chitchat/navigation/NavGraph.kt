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
    val sharedChatViewModel: ChatViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
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

        composable(route = Screen.Login.route) {
            LoginScreen(viewModel = authViewModel, onOtpSentNav = { phone -> navController.navigate(Screen.Otp.passPhone(phone)) })
        }

        composable(route = Screen.Otp.route, arguments = listOf(navArgument("phone") { type = NavType.StringType })) { entry ->
            val phone = entry.arguments?.getString("phone") ?: ""
            OtpScreen(phoneNumber = phone, viewModel = authViewModel, onVerifiedNav = { navController.navigate(Screen.ProfileSetup.route) })
        }

        composable(route = Screen.ProfileSetup.route) {
            ProfileSetupScreen(viewModel = authViewModel, onProfileFinishedNav = {
                navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } }
            })
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onChatClicked = { name, id, isGroup -> navController.navigate(Screen.Chat.passChatDetails(name, id, isGroup)) },
                onFabClicked = { navController.navigate(Screen.SelectContact.route) },
                onSettingsClicked = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType },
                navArgument("isGroup") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Chat"
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val isGroup = backStackEntry.arguments?.getBoolean("isGroup") ?: false

            ChatScreen(
                userName = name,
                receiverUid = id,
                isGroup = isGroup,
                viewModel = sharedChatViewModel,
                onBackClicked = { navController.popBackStack() },
                onNavigateToImagePreview = { navController.navigate(Screen.ImagePreview.route) },
                onNavigateToPdfPreview = { navController.navigate(Screen.PdfPreview.route) }
            )
        }

        composable(route = Screen.ImagePreview.route) { ImagePreviewScreen(viewModel = sharedChatViewModel, onBackClicked = { navController.popBackStack() }) }
        composable(route = Screen.PdfPreview.route) { PdfPreviewScreen(viewModel = sharedChatViewModel, onBackClicked = { navController.popBackStack() }) }

        composable(route = Screen.SelectContact.route) { SelectContactScreen(onBackClicked = { navController.popBackStack() }, onCreateGroupClicked = { navController.navigate(Screen.CreateGroup.route) }, onContactSelected = { name, uid -> navController.navigate(Screen.Chat.passChatDetails(name, uid, false)) { popUpTo(Screen.Dashboard.route) } }) }
        composable(route = Screen.CreateGroup.route) { CreateGroupScreen(onBackClicked = { navController.popBackStack() }, onDoneClicked = { navController.popBackStack() }) }
        composable(route = Screen.Settings.route) { SettingsScreen(onBackClicked = { navController.popBackStack() }, onAccountClicked = { navController.navigate(Screen.AccountSettings.route) }, onChatsClicked = { navController.navigate(Screen.ChatsSettings.route) }, onNotificationsClicked = { navController.navigate(Screen.NotificationsSettings.route) }) }
        composable(route = Screen.AccountSettings.route) { AccountSettingsScreen(onBackClicked = { navController.popBackStack() }) }
        composable(route = Screen.ChatsSettings.route) { ChatsSettingsScreen(onBackClicked = { navController.popBackStack() }, isDarkMode = isDarkMode, onThemeChanged = onThemeChanged) }
        composable(route = Screen.NotificationsSettings.route) { NotificationsSettingsScreen(onBackClicked = { navController.popBackStack() }) }
    }
}
