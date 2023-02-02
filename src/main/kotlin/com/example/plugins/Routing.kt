package com.example.plugins

import com.example.data.repository.user.UserRepository
import com.example.routes.userCreate
import com.example.service.UserService
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val userService: UserService by inject()
    routing {
        userCreate(userService)
    }
}
