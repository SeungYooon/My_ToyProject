package com.example.mytoyproject.network.api

import com.example.mytoyproject.network.model.User
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/users/{username}")
    suspend fun getUser(@Path("username") username: String): User
}