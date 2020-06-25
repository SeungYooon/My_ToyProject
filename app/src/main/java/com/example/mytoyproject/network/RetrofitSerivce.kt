package com.example.mytoyproject

import com.example.mytoyproject.network.model.GithubItem
import com.example.mytoyproject.network.model.Item
import com.example.mytoyproject.network.model.User
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    /*    @GET("/search/repositories")
        fun requestData(@Query("q") name: String): Call<GithubItem>*/
    @GET("/search/repositories")
    fun requestData(@Query("q") name: String): Single<GithubItem>

    @GET("/repos/{owner}/{repo}")
    fun requestOwner(@Path("owner") owner: String, @Path("repo") repo: String): Single<Item>

    @GET("/users/{username}")
    fun requestUser(@Path("username") username: String): Single<User>
}