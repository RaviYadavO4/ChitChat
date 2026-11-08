package com.portfolio.chitchat.data.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val type: String = "text", // text, image, pdf
    val fileBase64: String = "",
    val timestamp: Long = 0L,
    val status: String = "sent", // sent, delivered, read
    val deletedBy: List<String> = emptyList() // List coordinates holding UIDs who deleted locally
)
