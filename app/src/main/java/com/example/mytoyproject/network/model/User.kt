package com.example.mytoyproject.network.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("followers")
    var followers: Int,
    @SerializedName("following")
    var following: Int
)