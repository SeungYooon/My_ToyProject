package com.example.mytoyproject.network.api


import com.example.mytoyproject.network.model.GithubItem
import com.example.mytoyproject.network.model.Item
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepoService {

    @GET("/search/repositories")
    fun getRepo(@Query("q") name: String): Single<GithubItem>
}