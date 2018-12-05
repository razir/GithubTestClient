package com.anton.github2.datasource.content.user

import com.anton.github2.data.entity.UserProfile
import com.anton.github2.datasource.content.GithubRestApi
import io.reactivex.Single

interface UserRemoteRepository {

    fun getProfile(): Single<UserProfile>
}

class UserRemoteRepositoryImpl(private val api: GithubRestApi) :
    UserRemoteRepository {

    override fun getProfile(): Single<UserProfile> {
        return api.getUserProfile()
    }
}


