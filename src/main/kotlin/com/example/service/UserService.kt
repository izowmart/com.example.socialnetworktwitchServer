package com.example.service

import com.example.data.models.User
import com.example.data.repository.user.UserRepository
import com.example.data.requests.CreateAccountRequest

class UserService(
    private val userRepository: UserRepository
) {
    suspend fun doesUserWithEmailExist(email: String): Boolean{
        return userRepository.getUserByEmail(email) != null
    }
    suspend fun getUserByEmail(email: String): User?{
        return userRepository.getUserByEmail(email)
    }
    fun isValidPassword(enteredPassword: String, actualPassword : String): Boolean{
        return enteredPassword == actualPassword
    }
    suspend fun createUser(request: CreateAccountRequest){
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                gitHubUrl = null,
                instagramUrl = null,
                linkedInUrl = null
            )
        )
    }
    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvent{
        if(request.email.isBlank() || request.password.isBlank() || request.username.isBlank()){
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    sealed class ValidationEvent{
        object ErrorFieldEmpty : ValidationEvent()
        object Success : ValidationEvent()
    }
}