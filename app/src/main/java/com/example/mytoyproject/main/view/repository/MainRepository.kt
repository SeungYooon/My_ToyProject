package com.example.mytoyproject.main.view.repository

import com.example.mytoyproject.network.api.RepoHelper
import com.example.mytoyproject.network.model.Item
import io.reactivex.Single

class MainRepository(private val repoHelper: RepoHelper) {

    suspend fun getRepo(name: String) = repoHelper.getRepo(name)

    suspend fun getOwner(owner: String, repo: String) = repoHelper.getOwner(owner, repo)
}