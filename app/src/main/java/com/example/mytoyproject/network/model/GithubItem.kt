package com.example.mytoyproject.network.model

import com.google.gson.annotations.SerializedName


data class GithubItem(
    var items: ArrayList<Item>,
    @SerializedName("total_count")
    var totalCount: Int
)

data class Item(
    @SerializedName("name")
    var repositoryName: String,
    @SerializedName("owner")
    var owner: Owner,
    @SerializedName("language")
    var language: String,
    @SerializedName("stargazers_count")
    var stargazersCount: Int,
    @SerializedName("description")
    var description: String
)

data class Owner(
    @SerializedName("login")
    var ownerName: String,
    @SerializedName("avatar_url")
    var avatarUrl: String
)

