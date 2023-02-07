package com.example.routes

import com.example.data.requests.CreatePostRequest
import com.example.data.responses.BasicApiResponse
import com.example.service.PostService
import com.example.util.Constants
import com.example.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.createPost(
    postService: PostService,
) {
    authenticate {
        post("/api/post/create") {
            val createPostRequest: CreatePostRequest? = null

            createPostRequest?.let { request ->
                val createPostAcknowledgement = postService.createPost(
                    request = request,
                    userId = call.userId,
                    imageUrl = postPictureUrl
                )
                if (createPostAcknowledgement) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${Constants.POST_PICTURE_PATH}/$fileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}

fun Route.getPostsForProfile(
    postService: PostService,
) {
    authenticate {
        get("/api/user/posts") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

            val posts = postService.getPostsForProfile(
                ownUserId = call.userId,
                page = page,
                userId = userId ?: call.userId,
                pageSize = pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}

fun Route.getPostsForFollows(
    postService: PostService,
) {
    authenticate {
        get("/api/post/get") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

            val posts = postService.getPostsForFollows(
                ownUserId = call.userId,
                page = page,
                pageSize = pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}

fun Route.deletePost(
    postService: PostService,
    likeService: LikeService,
    commentService: CommentService
) {
    authenticate {
        delete("api/post/delete") {
            val postId = call.parameters["postId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val post = postService.getPost(postId)
            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }

            if (post.userId == call.userId) {
                postService.deletePost(postId)
                likeService.deleteLikesForPost(postId)
                commentService.deleteCommentsForPost(postId)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

}

fun Route.getPostDetails(postService: PostService){
    get("/api/post/details"){
        val postId = call.parameters["postId0"] ?: kotlin.run{
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val post = postService.getPostDetails(userId = call.userId, postId = postId) ?: kotlin.run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        call.respond(
            HttpStatusCode.OK,
            BasicApiResponse(
                successful = true,
                data = post
            )
        )
    }
}