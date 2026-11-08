package com.portfolio.chitchat.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portfolio.chitchat.data.model.Message
import com.portfolio.chitchat.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()

    private val _conversationMessages = MutableStateFlow<List<Message>>(emptyList())
    val conversationMessages: StateFlow<List<Message>> = _conversationMessages

    // Activates persistent pipeline hooks for instant text/media updates
    fun loadAndWatchMessages(receiverUid: String) {
        viewModelScope.launch {
            repository.listenToMessages(receiverUid)
                .catch { _conversationMessages.value = emptyList() }
                .collect { messages ->
                    _conversationMessages.value = messages
                }
        }
    }

    fun dispatchMessage(receiverUid: String, text: String, type: String = "text", base64Payload: String = "") {
        if (text.isNotBlank() || base64Payload.isNotBlank()) {
            repository.sendMessage(receiverUid, text, type, base64Payload) { success ->
                // Visual transmission indicators or local trace updates can be set here if required
            }
        }
    }
}
