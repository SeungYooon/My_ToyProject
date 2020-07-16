package com.example.mytoyproject.network.model

import com.example.mytoyproject.network.api.UserService
import io.reactivex.Single

class UserModelImpl(private val service: UserService) : UserModel {
    override fun getOwner(owner: String, repo: String): Single<Item> {
        return service.getOwner(owner = owner, repo = repo)
    }
    override fun getUser(username: String): Single<User> {
        return service.getUser(username = username)
    }
}