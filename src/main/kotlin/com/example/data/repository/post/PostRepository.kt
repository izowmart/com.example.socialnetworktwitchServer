package com.example.data.repository.post

import com.example.data.models.Post
import com.example.data.responses.PostResponse
import com.example.util.Constants

interface PostRepository {

    suspend fun createPost(post: Post): Boolean
    suspend fun deletePost(postId : String)
    suspend fun getPostsForFollows(
        ownUserId: String,
        page:Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ):List<PostResponse>

    suspend fun getPostsForProfile(
        ownUserId: String,
        userId:String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPost(postId:String): Post?
    suspend fun getPostDetails(userId: String, postId: String): PostResponse?
}