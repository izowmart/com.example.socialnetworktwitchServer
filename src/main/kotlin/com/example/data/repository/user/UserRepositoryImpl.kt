package com.example.data.repository.user

import com.example.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase

class UserRepositoryImpl(
    db: CoroutineDatabase
): UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun createUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun searchForUsers(query: String): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(userIds: List<String>): List<User> {
        TODO("Not yet implemented")
    }
    override suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {
        TODO("Not yet implemented")
    }
}