package com.portfolio.chitchat.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Otp : Screen("otp_screen/{phone}") {
        fun passPhone(phone: String) = "otp_screen/$phone"
    }
    object ProfileSetup : Screen("profile_setup_screen")
    object Dashboard : Screen("dashboard_screen")
    object Chat : Screen("chat_screen/{name}") {
        fun passName(name: String) = "chat_screen/$name"
    }
    object VoiceCall : Screen("voice_call_screen/{name}") {
        fun passName(name: String) = "voice_call_screen/$name"
    }
    object VideoCall : Screen("video_call_screen/{name}") {
        fun passName(name: String) = "video_call_screen/$name"
    }
    object StatusViewer : Screen("status_viewer_screen/{name}") {
        fun passName(name: String) = "status_viewer_screen/$name"
    }
    object SelectContact : Screen("select_contact_screen")
    object CreateGroup : Screen("create_group_screen")
    object Settings : Screen("settings_screen")

    object AccountSettings : Screen("account_settings_screen")
    object ChatsSettings : Screen("chats_settings_screen")
    object NotificationsSettings : Screen("notifications_settings_screen")
}
