package com.example.service

import com.example.data.models.Post
import com.example.data.repository.post.PostRepository
import com.example.data.requests.CreatePostRequest
import com.example.data.responses.PostResponse
import com.example.util.Constants

class PostService(
    private val repository: PostRepository
) {
    suspend fun createPost(request: CreatePostRequest, userId: String, imageUrl:String): Boolean{
        return repository.createPost(
            Post(
                imageUrl = imageUrl,
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }
    suspend fun deletePost(postId : String){
        repository.deletePost(postId)
    }
    suspend fun getPostsForFollows(
        ownUserId: String,
        page:Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ):List<PostResponse>{
        return repository.getPostsByFollows(ownUserId,page,pageSize)
    }

    suspend fun getPostsForProfile(
        ownUserId: String,
        userId:String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<PostResponse>{
        return repository.getPostsForProfile(ownUserId,userId,page,pageSize)
    }

    suspend fun getPost(postId:String): Post?{
        return repository.getPost(postId)
    }
    suspend fun getPostDetails(userId: String, postId: String): PostResponse?{
        return repository.getPostDetails(userId,postId)
    }
}