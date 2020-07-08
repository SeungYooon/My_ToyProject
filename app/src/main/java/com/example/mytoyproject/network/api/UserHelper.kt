package com.example.mytoyproject.network.api

class UserHelper(private val userService: UserService) {

    suspend fun getUser(username: String) = userService.getUser(username)
}