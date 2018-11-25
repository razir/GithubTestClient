package com.anton.medium.datasource.auth

import com.anton.medium.BuildConfig
import com.anton.medium.data.api.response.TokenResponse

interface MediumAuthRemoteRepository {

    suspend fun getTokenByCode(code: String): TokenResponse

    suspend fun getTokenByRefreshToken(refreshToken: String): TokenResponse

}

class MediumAuthRemoteRepositoryImpl(private val api: MediumAuthApi) : MediumAuthRemoteRepository {
    override suspend fun getTokenByCode(code: String): TokenResponse {
        return api.getTokenByCode(code, BuildConfig.MEDIUM_CLIENT_ID, BuildConfig.MEDIUM_SECRET).await()
    }

    override suspend fun getTokenByRefreshToken(refreshToken: String): TokenResponse {
        return api.getTokenByRefreshToken(refreshToken, BuildConfig.MEDIUM_CLIENT_ID, BuildConfig.MEDIUM_SECRET).await()
    }
}