package com.anton.github.datasource.content.user

import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.GithubRestApi

interface UserRemoteRepository {

    suspend fun getProfile(): UserProfile
}

class UserRemoteRepositoryImpl(private val api: GithubRestApi) :
    UserRemoteRepository {

    override suspend fun getProfile(): UserProfile {
        return api.getUserProfile().await()
    }
}


