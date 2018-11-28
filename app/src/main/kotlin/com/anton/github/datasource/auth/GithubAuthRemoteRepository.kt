package com.anton.github.datasource.auth

import com.anton.github.BuildConfig
import com.anton.github.data.api.response.TokenResponse

interface GithubAuthRemoteRepository {

    suspend fun getTokenByCode(code: String): TokenResponse

}


class GithubAuthRemoteRepositoryImpl(private val api: GithubAuthApi) : GithubAuthRemoteRepository {
    override suspend fun getTokenByCode(code: String): TokenResponse {
        return api.getTokenByCode(code, BuildConfig.MEDIUM_CLIENT_ID, BuildConfig.MEDIUM_SECRET).await()
    }
}