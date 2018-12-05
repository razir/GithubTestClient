package com.anton.github.datasource.auth

import com.anton.github.BuildConfig
import com.anton.github.data.api.response.TokenResponse
import io.reactivex.Single

interface GithubAuthRemoteRepository {

    fun getTokenByCode(code: String): Single<TokenResponse>
}

class GithubAuthRemoteRepositoryImpl(private val api: GithubAuthApi) : GithubAuthRemoteRepository {
    override fun getTokenByCode(code: String): Single<TokenResponse> {
        return api.getTokenByCode(code, BuildConfig.GItHUB_CLIENT_ID, BuildConfig.GITHUB_SECRET)
    }
}