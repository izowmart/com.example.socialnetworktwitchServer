package com.example.data.repository.chat

import com.example.data.models.Chat
import com.example.data.models.Message
import com.example.data.models.User
import com.example.data.responses.ChatDto
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ChatRepositoryImpl(
    db : CoroutineDatabase
): ChatRepository {
    private val users = db.getCollection<User>()
    private val chats = db.getCollection<Chat>()
    private val messages = db.getCollection<Message>()

    override suspend fun getMessagesForChat(chatId: String, page: Int, pageSize: Int): List<Message> {
        return messages.find(Message::chatId eq chatId)
            .skip(page * pageSize)
            .limit(pageSize)
            .ascendingSort(Message::timestamp)
            .toList()

    }

    override suspend fun getChatsForUser(ownUserId: String): List<ChatDto> {
        return chats.find(Chat::userIds contains ownUserId)
            .descendingSort(Chat::timestamp)
            .toList()
            .map { chat ->
                val otherUserId = chat.userIds.find{it != ownUserId}
                val user = users.findOneById(otherUserId ?: "")
                val message = messages.findOneById(chat.lastMessageId)

                ChatDto(
                    chatId = chat.id,
                    remoteUserId = user?.id,
                    remoteUsername = user?.username,
                    remoteUserProfilePictureUrl = user?.profileImageUrl,
                    lastMessage = message?.text,
                    timestamp = message?.timestamp
                )
            }

    }

    override suspend fun doesChatBelongToUser(chatId: String, userId: String): Boolean {
        return chats.findOneById(chatId)?.userIds?.any { it == userId } == true
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}