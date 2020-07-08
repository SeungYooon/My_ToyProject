package com.example.mytoyproject.network.model

import com.google.gson.annotations.SerializedName

data class GithubItem(
    val items: ArrayList<Item>,
    @SerializedName("total_count")
    val totalCount: Int
)

data class Item(
    @SerializedName("name")
    val repositoryName: String,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("language")
    val language: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("description")
    val description: String
)

data class Owner(
    @SerializedName("login")
    val ownerName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)