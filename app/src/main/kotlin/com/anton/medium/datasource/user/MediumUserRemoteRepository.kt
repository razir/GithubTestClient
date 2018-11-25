package com.anton.medium.datasource.user

import com.anton.medium.data.entity.UserProfile
import com.anton.medium.datasource.content.MediumContentApi

interface MediumUserRemoteRepository {

    suspend fun getProfile(): UserProfile?
}

class MediumUserRemoteRepositoryImpl(private val api: MediumContentApi) :
    MediumUserRemoteRepository {

    override suspend fun getProfile(): UserProfile? {
        return api.getUserProfile().await().data
    }
}


