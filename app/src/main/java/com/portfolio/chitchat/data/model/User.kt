package com.portfolio.chitchat.data.model

data class User(
    val uid: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val profileImageBase64: String = "", // Holds the complete converted image string matrix
    val statusText: String = "Hey there! I am using ChitChat.",
    val deviceId: String = ""
)
