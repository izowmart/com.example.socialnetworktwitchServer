package com.example.data.repository.user

import com.example.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.eq

class UserRepositoryImpl(
    db: CoroutineDatabase
) : UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun createUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.findOne(User::id eq id)
    }

    override suspend fun getUserByEmail(email: String): User? {
       return users.findOne(User::email eq email)
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