package com.example.mytoyproject.network.api

class RepoHelper(private val repoService: RepoService) {

    suspend fun getRepo(name: String) = repoService.getRepo(name)

    suspend fun getOwner(owner: String, repo: String) = repoService.getOwner(owner, repo)
}