package com.example.routes


import com.example.service.chat.ChatController
import com.example.service.chat.ChatService
import com.example.util.Constants
import com.example.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Route.getMessagesForChat(chatService: ChatService){
    authenticate{
        get("/api/chat/messages"){
            val chatId = call.parameters[QueryParams.PARAM_CHAT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?:  Constants.DEFAULT_PAGE_SIZE

            if (!chatService.doesChatBelongToUser(chatId, call.userId)){
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }

            val message = chatService.getMessagesForChat(chatId,page, pageSize)
            call.respond(HttpStatusCode.OK, message)
        }
    }
}

fun Route.getChatsForUser(chatService: ChatService){
    authenticate{
        get("/api/chats"){
            val chats = chatService.getChatsForUser(call.userId)
            call.respond(HttpStatusCode.OK,chats)
        }
    }
}

fun Route.chatWebSocket(chatController: ChatController){
    authenticate{
        webSocket("/api/chat/websocket") {
            println("Connecting via web socket")

        }
    }
}
