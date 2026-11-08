package com.portfolio.chitchat.data.model

data class Chat(
    val chatId: String = "",
    val isGroup: Boolean = false,
    val groupName: String = "",
    val groupAdmin: String = "",
    val members: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = 0L
)
