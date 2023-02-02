package com.example.data.repository.user

import com.example.data.models.User

class FakeUserRepository: UserRepository {

    private val users = mutableListOf<User>()

    override suspend fun createUser(user: User) {
       users.add(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.find{it.id == id}
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.find{it.email == email}
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
}