package com.portfolio.chitchat.data.repository

import android.app.Activity
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

    // Triggers Firebase SMS OTP generation matrix
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onOtpSent: (String) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-retrieval or instant validation parameters bypass
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
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

    // Validates the 6-Digit code input array against cloud tokens
    fun verifyOtp(
        otpCode: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val id = verificationId ?: return onFailure(Exception("Verification ID is missing."))
        val credential = PhoneAuthProvider.getCredential(id, otpCode)

        auth.signInWithCredential(credential)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Creates or updates the user profile record with Base64 payload details
    fun createUserProfile(
        name: String,
        base64Image: String,
        deviceId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val currentUid = auth.currentUser?.uid ?: return onComplete(false)
        val phone = auth.currentUser?.phoneNumber ?: ""

        val user = User(
            uid = currentUid,
            phoneNumber = phone,
            name = name,
            profileImageBase64 = base64Image,
            statusText = "Hey there! I am using ChitChat.",
            deviceId = deviceId
        )

        firestore.collection("users").document(currentUid)
            .set(user)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getCurrentUserUid(): String? = auth.currentUser?.uid
}
