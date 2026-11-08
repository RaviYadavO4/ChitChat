package com.portfolio.chitchat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.portfolio.chitchat.data.model.Chat
import com.portfolio.chitchat.data.model.Message

class GroupRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // 1. Creates a brand new functional Group Channel Node in Cloud Firestore
    fun createGroupChannel(groupName: String, selectedMembers: List<String>, onComplete: (Boolean) -> Unit) {
        if (currentUid.isBlank() || groupName.isBlank()) return onComplete(false)

        val groupChatId = firestore.collection("chats").document().id
        val allParticipantsList = selectedMembers.toMutableList().apply { add(currentUid) }

        val newGroupChat = Chat(
            chatId = groupChatId,
            isGroup = true,
            groupName = groupName,
            groupAdmin = currentUid,
            members = allParticipantsList,
            lastMessage = "👥 Group created successfully",
            lastMessageTimestamp = System.currentTimeMillis()
        )

        firestore.collection("chats").document(groupChatId)
            .set(newGroupChat)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // 2. Broadcasts message packets inside explicit group sub-collections channels
    fun sendGroupMessage(groupId: String, messageText: String, type: String = "text", base64Payload: String = "") {
        if (currentUid.isBlank() || groupId.isBlank()) return

        val msgRef = firestore.collection("chats").document(groupId).collection("messages").document()
        val messageInstance = Message(
            messageId = msgRef.id,
            senderId = currentUid,
            text = messageText,
            type = type,
            fileBase64 = base64Payload,
            timestamp = System.currentTimeMillis(),
            status = "sent"
        )

        msgRef.set(messageInstance).addOnSuccessListener {
            // Overwrites dashboard latest item reference for group feeds mapping
            firestore.collection("chats").document(groupId).update(
                mapOf(
                    "lastMessage" to messageText,
                    "lastMessageTimestamp" to System.currentTimeMillis()
                )
            )
        }
    }
}
