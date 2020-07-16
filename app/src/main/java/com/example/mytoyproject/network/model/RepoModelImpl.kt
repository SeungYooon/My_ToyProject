package com.example.mytoyproject.network.model

import com.example.mytoyproject.network.api.RepoService
import io.reactivex.Single

class RepoModelImpl(private val service: RepoService) : RepoModel {
    override fun getRepo(name: String): Single<GithubItem> {
        return service.getRepo(name = name)
    }
}