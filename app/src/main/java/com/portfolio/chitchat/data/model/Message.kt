package com.portfolio.chitchat.data.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val type: String = "text", // text, image, audio, video
    val fileBase64: String = "", // Stores image shares as continuous string payload blocks
    val timestamp: Long = 0L
)
