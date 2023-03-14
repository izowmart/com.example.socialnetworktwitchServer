package com.example.service.chat

import com.example.data.repository.chat.ChatRepository
import com.google.gson.Gson
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val repository: ChatRepository
) {
    private val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()

    fun onJoin(userId: String, socket:WebSocketSession){
        onlineUsers[userId] = socket
    }

    fun onDisconnect(userId: String){
        if (onlineUsers.contains(userId)){
            onlineUsers.remove(userId)
        }
    }

//    suspend fun sendMessage(ownUserId: String, gson: Gson, message:WsClientMessage){
//
//    }



}