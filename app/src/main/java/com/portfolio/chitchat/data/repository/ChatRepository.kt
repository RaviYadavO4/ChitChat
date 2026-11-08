package com.portfolio.chitchat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.portfolio.chitchat.data.model.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUid: String get() = auth.currentUser?.uid ?: ""

    fun getSingleChatId(receiverUid: String): String {
        return if (currentUid < receiverUid) "${currentUid}_$receiverUid" else "${receiverUid}_$currentUid"
    }

    fun sendMessage(receiverUid: String, messageText: String, type: String, fileBase64: String, onComplete: (Boolean) -> Unit) {
        if (currentUid.isBlank() || receiverUid.isBlank()) return onComplete(false)
        val chatId = getSingleChatId(receiverUid)
        val messageRef = firestore.collection("chats").document(chatId).collection("messages").document()

        val messageInstance = Message(
            messageId = messageRef.id,
            senderId = currentUid,
            text = messageText,
            type = type,
            fileBase64 = fileBase64,
            timestamp = System.currentTimeMillis(),
            status = "sent"
        )

        messageRef.set(messageInstance)
            .addOnSuccessListener {
                val chatSessionUpdate = hashMapOf(
                    "chatId" to chatId,
                    "isGroup" to false,
                    "groupName" to "",
                    "groupAdmin" to "",
                    "members" to listOf(currentUid, receiverUid),
                    "lastMessage" to if (type == "image") "🖼️ Photo" else if (type == "pdf") "📄 Document" else messageText,
                    "lastMessageTimestamp" to System.currentTimeMillis()
                )
                firestore.collection("chats").document(chatId).set(chatSessionUpdate)
                onComplete(true)
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun listenToMessages(receiverUid: String): Flow<List<Message>> = callbackFlow {
        val chatId = getSingleChatId(receiverUid)
        val queryListener = firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messagesList = mutableListOf<Message>()
                snapshot?.documents?.forEach { doc ->
                    try {
                        val msg = doc.toObject(Message::class.java)
                        if (msg != null) {
                            if (msg.senderId != currentUid) {
                                if (msg.status == "sent") doc.reference.update("status", "delivered")
                                if (msg.status != "read") doc.reference.update("status", "read")
                            }
                            messagesList.add(msg)
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("ChitChat_Parse", "Error: ${e.message}")
                    }
                }

                val cleanFilteredList = messagesList.filter { !it.deletedBy.contains(currentUid) }
                trySend(cleanFilteredList)
            }
        awaitClose { queryListener.remove() }
    }

    fun editMessageText(receiverUid: String, messageId: String, newText: String, onComplete: (Boolean) -> Unit) {
        val chatId = getSingleChatId(receiverUid)
        firestore.collection("chats").document(chatId).collection("messages").document(messageId)
            .update("text", "$newText (Edited)")
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteMessageForEveryone(receiverUid: String, messageId: String, onComplete: (Boolean) -> Unit) {
        val chatId = getSingleChatId(receiverUid)
        val updateData = hashMapOf<String, Any>(
            "text" to "🚫 This message was deleted",
            "type" to "text",
            "fileBase64" to ""
        )
        firestore.collection("chats").document(chatId).collection("messages").document(messageId)
            .update(updateData)
            .addOnSuccessListener {
                firestore.collection("chats").document(chatId).update("lastMessage", "🚫 Message deleted")
                onComplete(true)
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteMessageForMeOnly(receiverUid: String, messageId: String, onComplete: (Boolean) -> Unit) {
        val chatId = getSingleChatId(receiverUid)
        firestore.collection("chats").document(chatId).collection("messages").document(messageId)
            .update("deletedBy", FieldValue.arrayUnion(currentUid))
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
