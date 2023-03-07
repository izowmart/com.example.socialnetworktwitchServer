package com.example.service.chat

import com.example.data.models.Message
import com.example.data.repository.chat.ChatRepository
import com.example.data.responses.ChatDto

class ChatService(
    private val chatRepository: ChatRepository
){
     suspend fun getMessagesForChat(chatId: String, page: Int, pageSize: Int): List<Message> {
      return chatRepository.getMessagesForChat(chatId, page, pageSize)
    }

     suspend fun getChatsForUser(ownUserId: String): List<ChatDto> {
        return chatRepository.getChatsForUser(ownUserId)
    }

     suspend fun doesChatBelongToUser(chatId: String, userId: String): Boolean {
        return chatRepository.doesChatBelongToUser(chatId, userId)
    }

}