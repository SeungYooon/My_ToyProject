package com.example.mytoyproject.network.model

import io.reactivex.Single

interface RepoModel {
    fun getRepo(name: String): Single<GithubItem>
}