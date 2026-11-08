package com.portfolio.chitchat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.portfolio.chitchat.data.model.Chat
import com.portfolio.chitchat.data.model.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GroupRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

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

    fun sendGroupMessage(groupId: String, messageText: String, type: String = "text", base64Payload: String = "") {
        if (currentUid.isBlank() || groupId.isBlank()) return

        firestore.collection("users").document(currentUid).get().addOnSuccessListener { doc ->
            val senderName = doc.getString("name") ?: "Member"
            val msgRef = firestore.collection("chats").document(groupId).collection("messages").document()
            val messageInstance = Message(
                messageId = msgRef.id,
                senderId = currentUid,
                senderName = senderName,
                text = messageText,
                type = type,
                fileBase64 = base64Payload,
                timestamp = System.currentTimeMillis(),
                status = "sent"
            )

            msgRef.set(messageInstance).addOnSuccessListener {
                val previewText = if (type == "image") "🖼️ Photo" else if (type == "pdf") "📄 Document" else messageText
                firestore.collection("chats").document(groupId).update(
                    mapOf(
                        "lastMessage" to "$senderName: $previewText",
                        "lastMessageTimestamp" to System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun listenToGroupMessages(groupId: String): Flow<List<Message>> = callbackFlow {
        val queryListener = firestore.collection("chats").document(groupId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messagesList = mutableListOf<Message>()
                snapshot?.documents?.forEach { doc ->
                    val msg = doc.toObject(Message::class.java)
                    if (msg != null && !msg.deletedBy.contains(currentUid)) {
                        messagesList.add(msg)
                    }
                }
                trySend(messagesList)
            }
        awaitClose { queryListener.remove() }
    }
}
