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

    private val _isChatLoading = MutableStateFlow(true)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading

    private val _activePreviewPayload = MutableStateFlow("")
    val activePreviewPayload: StateFlow<String> = _activePreviewPayload

    fun loadAndWatchMessages(receiverUid: String) {
        _isChatLoading.value = true
        viewModelScope.launch {
            repository.listenToMessages(receiverUid)
                .catch {
                    _conversationMessages.value = emptyList()
                    _isChatLoading.value = false
                }
                .collect { messages ->
                    _conversationMessages.value = messages
                    _isChatLoading.value = false
                }
        }
    }

    fun dispatchMessage(receiverUid: String, text: String, type: String = "text", base64Payload: String = "") {
        repository.sendMessage(receiverUid, text, type, base64Payload) { }
    }

    fun cachePreviewPayload(payload: String) {
        _activePreviewPayload.value = payload
    }

    fun requestMessageEdit(receiverUid: String, messageId: String, updatedText: String) {
        repository.editMessageText(receiverUid, messageId, updatedText) { }
    }

    fun requestDeleteForEveryone(receiverUid: String, messageId: String) {
        repository.deleteMessageForEveryone(receiverUid, messageId) { }
    }

    fun requestDeleteForMe(receiverUid: String, messageId: String) {
        repository.deleteMessageForMeOnly(receiverUid, messageId) { }
    }
}
