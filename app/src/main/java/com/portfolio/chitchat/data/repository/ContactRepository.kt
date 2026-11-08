package com.portfolio.chitchat.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.portfolio.chitchat.data.model.User

class ContactRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUid: String get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Fetches live cross-device production data profiles directly
    fun fetchAllChitChatContacts(onResult: (List<User>) -> Unit) {
        if (currentUid.isBlank()) {
            Log.e("ChitChat_Contacts", "Fetch aborted: Session UID is blank.")
            return onResult(emptyList())
        }

        firestore.collection("users").get()
            .addOnSuccessListener { snapshot ->
                // Clean data extraction converting document nodes directly
                val usersList = snapshot.toObjects(User::class.java) ?: emptyList()

                // Absolute filter ensuring you only see the other user's identity card
                val filteredList = usersList.filter { it.uid != currentUid }

                Log.d("ChitChat_Contacts", "Database Fetch Complete - Total: ${usersList.size}, Renderable: ${filteredList.size}")
                onResult(filteredList)
            }
            .addOnFailureListener { exception ->
                Log.e("ChitChat_Contacts", "Firestore Fetch Failure: ${exception.message}")
                onResult(emptyList())
            }
    }
}
