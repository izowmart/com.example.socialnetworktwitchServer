package com.example.routes

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.createPost(
    postService: PostService,
){
    authenticate {
        post("/api/post/create"){
            val createPostRequest: CreatePostRequest? = null
        }
    }
}