package com.example.mytoyproject.network.api

import com.example.mytoyproject.network.model.Item
import com.example.mytoyproject.network.model.User
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/repos/{owner}/{repo}")
    fun getOwner(@Path("owner") owner: String, @Path("repo") repo: String): Single<Item>

    @GET("/users/{username}")
    fun getUser(@Path("username") username: String): Single<User>
}