package com.portfolio.chitchat.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portfolio.chitchat.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object OtpSent : AuthState()
    object Verified : AuthState()
    data class ProfileCreated(val success: Boolean) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    var userPhoneNumber: String = ""
        private set

    fun sendOtp(phone: String, activity: Activity) {
        userPhoneNumber = phone
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            repository.sendOtp(
                phoneNumber = phone,
                activity = activity,
                onOtpSent = { _authState.value = AuthState.OtpSent },
                onVerificationFailed = { _authState.value = AuthState.Error(it.message ?: "OTP Failed") }
            )
        }
    }

    fun verifyOtp(code: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            repository.verifyOtp(
                otpCode = code,
                onSuccess = { _authState.value = AuthState.Verified },
                onFailure = { _authState.value = AuthState.Error(it.message ?: "Invalid OTP") }
            )
        }
    }

    fun saveProfile(name: String, base64Image: String, deviceId: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            repository.createUserProfile(name, base64Image, deviceId) { success ->
                if (success) {
                    _authState.value = AuthState.ProfileCreated(true)
                } else {
                    _authState.value = AuthState.Error("Profile Creation Failed")
                }
            }
        }
    }
}
