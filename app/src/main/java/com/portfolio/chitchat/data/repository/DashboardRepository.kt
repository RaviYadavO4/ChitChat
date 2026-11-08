package com.portfolio.chitchat.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.portfolio.chitchat.data.model.Chat
import com.portfolio.chitchat.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DashboardRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUid: String get() = auth.currentUser?.uid ?: ""

    fun listenToChatSessions(): Flow<List<Chat>> = callbackFlow {
        if (currentUid.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        // Production-Grade Live Listener syncing dynamic updates instantly
        val queryListener = firestore.collection("chats")
            .whereArrayContains("members", currentUid) // Tracks sessions where current user is a participant
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChitChat_Dashboard", "Dashboard snapshot listener failed: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                val chatsList = snapshot?.toObjects(Chat::class.java) ?: emptyList()
                Log.d("ChitChat_Dashboard", "Total active feed chats fetched: ${chatsList.size}")
                trySend(chatsList)
            }

        awaitClose { queryListener.remove() }
    }

    fun fetchParticipantInfo(participantUid: String, onResult: (User?) -> Unit) {
        firestore.collection("users").document(participantUid).get()
            .addOnSuccessListener { document ->
                val userProfile = document.toObject(User::class.java)
                onResult(userProfile)
            }
            .addOnFailureListener { onResult(null) }
    }
}
