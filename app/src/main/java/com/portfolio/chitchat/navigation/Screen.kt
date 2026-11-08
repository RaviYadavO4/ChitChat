package com.portfolio.chitchat.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Otp : Screen("otp_screen/{phone}") {
        fun passPhone(phone: String) = "otp_screen/$phone"
    }
    object ProfileSetup : Screen("profile_setup_screen")
    object Dashboard : Screen("dashboard_screen")
    object Chat : Screen("chat_screen/{name}/{uid}") {
        fun passNameAndUid(name: String, uid: String) = "chat_screen/$name/$uid"
    }
    object SelectContact : Screen("select_contact_screen")
    object CreateGroup : Screen("create_group_screen")
    object Settings : Screen("settings_screen")

    object AccountSettings : Screen("account_settings_screen")
    object ChatsSettings : Screen("chats_settings_screen")
    object NotificationsSettings : Screen("notifications_settings_screen")

    object ImagePreview : Screen("image_preview_screen")
    object PdfPreview : Screen("pdf_preview_screen")
}
