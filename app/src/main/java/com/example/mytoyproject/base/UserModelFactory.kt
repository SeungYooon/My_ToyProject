package com.example.mytoyproject.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytoyproject.main.view.repository.UserRepository
import com.example.mytoyproject.main.view.viewmodel.UserViewModel
import com.example.mytoyproject.network.api.UserHelper
import java.lang.IllegalArgumentException

class UserModelFactory(private val userHelper: UserHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(UserRepository(userHelper)) as T
        }
        throw  IllegalArgumentException("Unknown class name")
    }
}