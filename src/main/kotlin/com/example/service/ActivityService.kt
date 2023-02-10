package com.example.service

import com.example.data.models.Activity
import com.example.data.repository.activity.ActivityRepository
import com.example.data.repository.post.PostRepository
import com.example.data.responses.ActivityResponse
import com.example.util.Constants.DEFAULT_ACTIVITY_PAGE_SIZE

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val postRepository: PostRepository
) {
    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<ActivityResponse>{
        return activityRepository.getActivitiesForUser(userId,page,pageSize)
    }

    suspend fun addCommentActivity(
        byUserId: String,
        postId: String
    ):Boolean{
        val userIdOfPost = postRepository.getPost(postId) ?: return false
        if (byUserId == userIdOfPost){ // todo
            return false
        }
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = userIdOfPost,
                type = 1,
                parentId = postId
            )
        )
        return true

    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentType: ParentType,
        parentId: String
    ): Boolean{
        val toUserId = when(parentType){
            is ParentType.Post -> {

            }
        }
    }

    suspend fun createActivity(activity: Activity){
        activityRepository.createActivity(activity)
    }
    suspend fun deleteActivity(activityId: String): Boolean{
        return activityRepository.deleteActivity(activityId)
    }
}