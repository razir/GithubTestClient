package com.anton.medium.domain.usecase

import com.anton.medium.data.api.response.TokenResponse
import com.anton.medium.datasource.auth.MediumAuthLocalRepository
import com.anton.medium.datasource.auth.MediumAuthRemoteRepository
import java.lang.Exception

interface RefreshTokenUseCase {

    suspend fun run(): ResultUseCase
}

class RefreshTokenUseCaseImpl(
    private val authLocalRepository: MediumAuthLocalRepository,
    private val authRemoteRepository: MediumAuthRemoteRepository
) : RefreshTokenUseCase {

    override suspend fun run(): ResultUseCase {
        try {
            authLocalRepository.token?.let {
                if (isTokenExpired(it)) {
                    authLocalRepository.token?.refreshToken?.let { refreshToken ->
                        val newToken = authRemoteRepository.getTokenByRefreshToken(refreshToken)
                        authLocalRepository.token = newToken
                        return CompletedUseCase
                    }
                    return ErrorUseCase()
                } else {
                    return CompletedUseCase
                }
            } ?: return ErrorUseCase()
        } catch (e: Exception) {
            return ErrorUseCase()
        }
    }

    private fun isTokenExpired(token: TokenResponse): Boolean {
        return token.expiresAt ?: 0 <= System.currentTimeMillis() / 1000
    }
}