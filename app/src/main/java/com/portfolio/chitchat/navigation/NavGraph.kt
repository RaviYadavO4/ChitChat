package com.portfolio.chitchat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.portfolio.chitchat.ui.auth.*
import com.portfolio.chitchat.ui.dashboard.DashboardScreen
import com.portfolio.chitchat.ui.splash.SplashScreen
import com.portfolio.chitchat.ui.chat.ChatScreen
import com.portfolio.chitchat.ui.calling.*
import com.portfolio.chitchat.ui.status.StatusViewerScreen
import com.portfolio.chitchat.ui.contacts.*
import com.portfolio.chitchat.ui.settings.SettingsScreen
import com.portfolio.chitchat.ui.settings.sub.*
import kotlinx.coroutines.delay

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Core Splash & Auth routes stay exact same as previous clean implementation structure...
        composable(route = Screen.Splash.route) {
            SplashScreen()
            LaunchedEffect(key1 = true) {
                delay(3000L)
                navController.navigate(Screen.Login.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
            }
        }
        composable(route = Screen.Login.route) {
            LoginScreen(onNextClicked = { fullPhone -> navController.navigate(Screen.Otp.passPhone(fullPhone)) })
        }
        composable(route = Screen.Otp.route, arguments = listOf(navArgument("phone") { type = NavType.StringType })) { entry ->
            OtpScreen(phoneNumber = entry.arguments?.getString("phone") ?: "", onVerifyClicked = { navController.navigate(Screen.ProfileSetup.route) })
        }
        composable(route = Screen.ProfileSetup.route) {
            ProfileSetupScreen(onFinishClicked = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } } })
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onChatClicked = { name -> navController.navigate(Screen.Chat.passName(name)) },
                onStatusClicked = { name -> navController.navigate(Screen.StatusViewer.passName(name)) },
                onCallClicked = { name -> navController.navigate(Screen.VoiceCall.passName(name)) },
                onFabClicked = { navController.navigate(Screen.SelectContact.route) },
                onSettingsClicked = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Sub Settings Redirections Updates
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClicked = { navController.popBackStack() },
                onAccountClicked = { navController.navigate(Screen.AccountSettings.route) },
                onChatsClicked = { navController.navigate(Screen.ChatsSettings.route) },
                onNotificationsClicked = { navController.navigate(Screen.NotificationsSettings.route) }
            )
        }

        composable(route = Screen.ChatsSettings.route) {
            ChatsSettingsScreen(
                onBackClicked = { navController.popBackStack() },
                isDarkMode = isDarkMode,
                onThemeChanged = onThemeChanged
            )
        }

        // Keep standard placeholders for remaining ones to ensure compilation
        composable(route = Screen.AccountSettings.route) { AccountSettingsScreen(onBackClicked = { navController.popBackStack() }) }
        composable(route = Screen.NotificationsSettings.route) { NotificationsSettingsScreen(onBackClicked = { navController.popBackStack() }) }
        composable(route = Screen.Chat.route, arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry -> ChatScreen(userName = entry.arguments?.getString("name") ?: "User", onBackClicked = { navController.popBackStack() }) }
        composable(route = Screen.VoiceCall.route, arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry -> VoiceCallScreen(userName = entry.arguments?.getString("name") ?: "User", onEndCallClicked = { navController.popBackStack() }) }
        composable(route = Screen.VideoCall.route, arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry -> VideoCallScreen(userName = entry.arguments?.getString("name") ?: "User", onEndCallClicked = { navController.popBackStack() }) }
        composable(route = Screen.StatusViewer.route, arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry -> StatusViewerScreen(userName = entry.arguments?.getString("name") ?: "User", onStoryTimedOut = { navController.popBackStack() }) }
        composable(route = Screen.SelectContact.route) { SelectContactScreen(onBackClicked = { navController.popBackStack() }, onCreateGroupClicked = { navController.navigate(Screen.CreateGroup.route) }, onContactSelected = { name -> navController.navigate(Screen.Chat.passName(name)) }) }
        composable(route = Screen.CreateGroup.route) { CreateGroupScreen(onBackClicked = { navController.popBackStack() }, onDoneClicked = { navController.popBackStack() }) }
    }
}
