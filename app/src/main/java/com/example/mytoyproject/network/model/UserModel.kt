package com.example.mytoyproject.network.model

import io.reactivex.Single

interface UserModel {
    fun getOwner(owner: String, repo: String): Single<Item>

    fun getUser(username: String): Single<User>
}