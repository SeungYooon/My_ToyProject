package com.example.mytoyproject.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytoyproject.main.view.repository.MainRepository
import com.example.mytoyproject.main.view.viewmodel.MainViewModel
import com.example.mytoyproject.network.api.RepoHelper
import java.lang.IllegalArgumentException

class ViewModelFactory(private val repoHelper: RepoHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(repoHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}