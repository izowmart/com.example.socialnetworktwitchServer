package com.example.routes

import com.example.data.models.Activity
import com.example.data.requests.FollowUpdateRequest
import com.example.data.responses.BasicApiResponse
import com.example.data.util.ActivityType
import com.example.service.ActivityService
import com.example.service.FollowService
import com.example.util.ApiResponseMessages.USER_NOT_FOUND
import com.example.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUser(
    followService: FollowService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/following/follow"){
            val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val didUserExist = followService.followUserIfExists(request,call.userId)
            if (didUserExist){
                activityService.createActivity(
                    Activity(
                        timestamp = System.currentTimeMillis(),
                        byUserId = call.userId,
                        toUserId = request.followedUserId,
                        type = ActivityType.FollowedUser.type,
                        parentId = ""
                    )
                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            }else{
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}
fun Route.unfollowUser(followService: FollowService){
    authenticate {
        delete("/api/following/unfollow"){
            val userId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val didUserExist = followService.unfollowUserIfExists(userId,call.userId)
            if (didUserExist){
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            }else{
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}