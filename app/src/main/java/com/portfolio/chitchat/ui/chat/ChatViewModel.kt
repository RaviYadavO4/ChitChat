package com.portfolio.chitchat.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portfolio.chitchat.data.model.Message
import com.portfolio.chitchat.data.repository.ChatRepository
import com.portfolio.chitchat.data.repository.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val chatRepository = ChatRepository()
    private val groupRepository = GroupRepository()

    private val _conversationMessages = MutableStateFlow<List<Message>>(emptyList())
    val conversationMessages: StateFlow<List<Message>> = _conversationMessages

    private val _isChatLoading = MutableStateFlow(true)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading

    private val _activePreviewPayload = MutableStateFlow("")
    val activePreviewPayload: StateFlow<String> = _activePreviewPayload

    fun loadAndWatchMessages(id: String, isGroup: Boolean) {
        _isChatLoading.value = true
        viewModelScope.launch {
            val flow = if (isGroup) {
                groupRepository.listenToGroupMessages(id)
            } else {
                chatRepository.listenToMessages(id)
            }

            flow.catch {
                _conversationMessages.value = emptyList()
                _isChatLoading.value = false
            }.collect { messages ->
                _conversationMessages.value = messages
                _isChatLoading.value = false
            }
        }
    }

    fun dispatchMessage(id: String, isGroup: Boolean, text: String, type: String = "text", base64Payload: String = "") {
        if (text.isNotBlank() || base64Payload.isNotBlank()) {
            if (isGroup) {
                groupRepository.sendGroupMessage(id, text, type, base64Payload)
            } else {
                chatRepository.sendMessage(id, text, type, base64Payload) { }
            }
        }
    }

    fun cachePreviewPayload(payload: String) {
        _activePreviewPayload.value = payload
    }

    fun requestMessageEdit(id: String, isGroup: Boolean, messageId: String, updatedText: String) {
        if (!isGroup) {
            chatRepository.editMessageText(id, messageId, updatedText) { }
        }
    }

    fun requestDeleteForEveryone(id: String, isGroup: Boolean, messageId: String) {
        if (!isGroup) {
            chatRepository.deleteMessageForEveryone(id, messageId) { }
        }
    }

    fun requestDeleteForMe(id: String, isGroup: Boolean, messageId: String) {
        if (!isGroup) {
            chatRepository.deleteMessageForMeOnly(id, messageId) { }
        }
    }
}
