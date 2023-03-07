package com.example.data.repository.chat

import com.example.data.models.Message
import com.example.data.responses.ChatDto

interface ChatRepository {

    suspend fun getMessagesForChat(chatId: String, page:Int,pageSize: Int): List<Message>

    suspend fun getChatsForUser(ownUserId: String): List<ChatDto>

    suspend fun doesChatBelongToUser(chatId:String, userId:String): Boolean

    suspend fun insertMessage(message:Message)
}