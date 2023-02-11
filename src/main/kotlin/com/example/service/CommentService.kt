package com.example.service

import com.example.data.models.Comment
import com.example.data.repository.comment.CommentRepository
import com.example.data.repository.user.UserRepository
import com.example.data.requests.CreateCommentRequest
import com.example.data.responses.CommentResponse
import com.example.util.Constants

class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    suspend fun createComment(createCommentRequest: CreateCommentRequest, userId: String): ValidationEvent {
        createCommentRequest.apply {
            if (comment.isBlank() || postId.isBlank()){
                return ValidationEvent.ErrorFieldEmpty
            }
            if (comment.length > Constants.MAX_COMMENT_LENGTH){
                return ValidationEvent.ErrorCommentTooLong
            }
        }
        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        commentRepository.createComment(
            Comment(
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                likeCount = 0,
                userId = userId,
                comment = createCommentRequest.comment,
                postId = createCommentRequest.postId,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(commentId: String):Boolean {
        return commentRepository.deleteComment(commentId)
    }

    suspend fun deleteCommentsFromPost(postId: String) {
        commentRepository.deleteCommentsFromPost(postId)
    }

    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<CommentResponse> {
       return commentRepository.getCommentsForPost(postId,ownUserId)
    }

    suspend fun getComment(commentId: String): Comment? {
        return commentRepository.getComment(commentId)
    }

    sealed class ValidationEvent{
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object UserNotFound : ValidationEvent()
        object Success : ValidationEvent()
    }
}