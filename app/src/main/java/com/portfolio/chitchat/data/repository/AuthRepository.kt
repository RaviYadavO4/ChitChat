package com.portfolio.chitchat.data.repository

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.portfolio.chitchat.data.model.User
import java.util.concurrent.TimeUnit

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var verificationId: String? = null

    fun sendOtp(phoneNumber: String, activity: Activity, onOtpSent: (String) -> Unit, onVerificationFailed: (Exception) -> Unit) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
            override fun onVerificationFailed(e: FirebaseException) { onVerificationFailed(e) }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@AuthRepository.verificationId = verificationId
                onOtpSent(verificationId)
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otpCode: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val id = verificationId ?: return onFailure(Exception("Verification ID is missing."))
        val credential = PhoneAuthProvider.getCredential(id, otpCode)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // High-Fidelity Profile Writer Engine with Explicit Debug Callbacks
    fun createUserProfile(name: String, base64Image: String, deviceId: String, onComplete: (Boolean) -> Unit) {
        val currentUid = auth.currentUser?.uid
        if (currentUid == null) {
            Log.e("ChitChat_Auth", "Firestore upload aborted: Firebase Current User Session is NULL.")
            return onComplete(false)
        }
        val phone = auth.currentUser?.phoneNumber ?: ""

        val user = User(
            uid = currentUid,
            phoneNumber = phone,
            name = name,
            profileImageBase64 = base64Image,
            statusText = "Hey there! I am using ChitChat.",
            deviceId = deviceId
        )

        // Write document to firestore with absolute key validation
        firestore.collection("users").document(currentUid)
            .set(user)
            .addOnSuccessListener {
                Log.d("ChitChat_Auth", "Firestore Profile Success: Document created for UID: $currentUid")
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.e("ChitChat_Auth", "Firestore Profile Write Failed: ${exception.message}")
                onComplete(false)
            }
    }

    fun fetchExistingProfile(onResult: (User?) -> Unit) {
        val currentUid = auth.currentUser?.uid
        if (currentUid == null) {
            onResult(null)
            return
        }

        firestore.collection("users").document(currentUid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getCurrentUserUid(): String? = auth.currentUser?.uid
}
