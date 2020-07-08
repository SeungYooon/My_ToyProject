package com.example.mytoyproject.main.view.repository

import com.example.mytoyproject.network.api.UserHelper
import com.example.mytoyproject.network.model.User
import io.reactivex.Single

class UserRepository(private val userHelper: UserHelper) {

    suspend fun getUser(username: String) = userHelper.getUser(username)
}