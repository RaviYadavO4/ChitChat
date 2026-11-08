package com.portfolio.chitchat.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.portfolio.chitchat.data.model.Chat
import com.portfolio.chitchat.data.model.User
import com.portfolio.chitchat.data.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class ActiveFeedItem(
    val chatMeta: Chat,
    val participantUser: User?
)

class DashboardViewModel : ViewModel() {
    private val repository = DashboardRepository()
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _activeFeeds = MutableStateFlow<List<ActiveFeedItem>>(emptyList())
    val activeFeeds: StateFlow<List<ActiveFeedItem>> = _activeFeeds

    private val _isFeedLoading = MutableStateFlow(true)
    val isFeedLoading: StateFlow<Boolean> = _isFeedLoading

    init {
        monitorChatFeedUpdates()
    }

    private fun monitorChatFeedUpdates() {
        viewModelScope.launch {
            repository.listenToChatSessions()
                .catch { _activeFeeds.value = emptyList() }
                .collect { chats ->
                    val completeFeedItems = mutableListOf<ActiveFeedItem>()

                    if (chats.isEmpty()) {
                        _activeFeeds.value = emptyList()
                        _isFeedLoading.value = false
                        return@collect
                    }

                    chats.forEach { chat ->
                        if (chat.isGroup) {
                            completeFeedItems.add(ActiveFeedItem(chat, null))
                            if (completeFeedItems.size == chats.size) {
                                _activeFeeds.value = completeFeedItems.sortedByDescending { it.chatMeta.lastMessageTimestamp }
                                _isFeedLoading.value = false
                            }
                        } else {
                            val targetUid = chat.members.firstOrNull { it != currentUid } ?: ""
                            if (targetUid.isNotBlank()) {
                                repository.fetchParticipantInfo(targetUid) { resolvedUser ->
                                    completeFeedItems.add(ActiveFeedItem(chat, resolvedUser))
                                    if (completeFeedItems.size == chats.size) {
                                        _activeFeeds.value = completeFeedItems.sortedByDescending { it.chatMeta.lastMessageTimestamp }
                                        _isFeedLoading.value = false
                                    }
                                }
                            } else {
                                completeFeedItems.add(ActiveFeedItem(chat, null))
                                if (completeFeedItems.size == chats.size) {
                                    _activeFeeds.value = completeFeedItems.sortedByDescending { it.chatMeta.lastMessageTimestamp }
                                    _isFeedLoading.value = false
                                }
                            }
                        }
                    }
                }
        }
    }
}
