package com.portfolio.chitchat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.portfolio.chitchat.data.model.Chat
import com.portfolio.chitchat.data.model.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUid: String get() = auth.currentUser?.uid ?: ""

    // Generates a predictable distinct channel ID for 2 users
    private fun getSingleChatId(receiverUid: String): String {
        return if (currentUid < receiverUid) "${currentUid}_$receiverUid" else "${receiverUid}_$currentUid"
    }

    // Sends text or base64 encrypted payloads to cloud buckets
    fun sendMessage(
        receiverUid: String,
        messageText: String,
        type: String = "text",
        fileBase64: String = "",
        onComplete: (Boolean) -> Unit
    ) {
        if (currentUid.isBlank()) return onComplete(false)

        val chatId = getSingleChatId(receiverUid)
        val messageRef = firestore.collection("chats").document(chatId).collection("messages").document()

        val messageInstance = Message(
            messageId = messageRef.id,
            senderId = currentUid,
            text = messageText,
            type = type,
            fileBase64 = fileBase64,
            timestamp = System.currentTimeMillis()
        )

        // Save message sub-collection node
        messageRef.set(messageInstance)
            .addOnSuccessListener {
                // Update primary root session document indicators for the dashboard feed
                val chatSessionUpdate = mapOf(
                    "chatId" to chatId,
                    "isGroup" to false,
                    "members" to listOf(currentUid, receiverUid),
                    "lastMessage" to if (type == "text") messageText else "🖼️ Photo",
                    "lastMessageTimestamp" to System.currentTimeMillis()
                )
                firestore.collection("chats").document(chatId).set(chatSessionUpdate)
                onComplete(true)
            }
            .addOnFailureListener { onComplete(false) }
    }

    // Real-Time snapshot continuous connection flow stream engine mapping
    fun listenToMessages(receiverUid: String): Flow<List<Message>> = callbackFlow {
        val chatId = getSingleChatId(receiverUid)
        val queryListener = firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messagesList = snapshot?.toObjects(Message::class.java) ?: emptyList()
                trySend(messagesList)
            }

        awaitClose { queryListener.remove() } // Cleanup thread hook to eliminate background memory leaks
    }
}
