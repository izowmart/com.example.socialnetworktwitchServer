package com.example.service.chat

import com.example.data.repository.chat.ChatRepository
import com.example.data.websocket.WsClientMessage
import com.example.data.websocket.WsServerMessage
import com.example.util.WebSocketObject
import com.google.gson.Gson
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val repository: ChatRepository
) {
    private val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()

    fun onJoin(userId: String, socket: WebSocketSession) {
        onlineUsers[userId] = socket
    }

    fun onDisconnect(userId: String) {
        if (onlineUsers.contains(userId)) {
            onlineUsers.remove(userId)
        }
    }

    suspend fun sendMessage(ownUserId: String, gson: Gson, message: WsClientMessage) {
        val messageEntity = message.toMessage(ownUserId)
        val wsServerMessage = WsServerMessage(
            fromId = ownUserId,
            toId = message.toId,
            text = message.text,
            timestamp = System.currentTimeMillis(),
            chatId = message.chatId
        )

        val frameText = gson.toJson(wsServerMessage)
        onlineUsers[ownUserId]?.send(Frame.Text("${WebSocketObject.MESSAGE.ordinal}#$frameText"))
        onlineUsers[message.toId]?.send(Frame.Text("${WebSocketObject.MESSAGE.ordinal}#$frameText"))

        // If there is no chat between the two, make one. else update the current one
        if (!repository.doesChatByUsersExist(ownUserId, message.toId)) {
            val chatId = repository.insertChat(
                userId1 = ownUserId,
                userId2 = message.toId,
                messageId = messageEntity.id
            )
            repository.insertMessage(messageEntity.copy(chatId = chatId)) //since here is no existing chat
        }else{
            repository.insertMessage(messageEntity)
            message.chatId?.let{
                repository.updateLastMessageIdForChat(lastMessageId = messageEntity.id, chatId = message.chatId)
            }
        }

    }


}