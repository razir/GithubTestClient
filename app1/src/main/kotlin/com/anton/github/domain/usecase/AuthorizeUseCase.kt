package com.anton.github.domain.usecase

import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.auth.GithubAuthRemoteRepository
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserRemoteRepository

interface AuthorizeUseCase {
    suspend fun run(code: String): ResultUseCase<Unit>
}

class AuthorizeUseCaseImpl(
    private val tokenRemoteRepository: GithubAuthRemoteRepository,
    private val tokenLocalRepository: GithubAuthLocalRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userRemoteRemoteRepository: UserRemoteRepository
) : AuthorizeUseCase {

    override suspend fun run(code: String): ResultUseCase<Unit> {
        try {
            val token = tokenRemoteRepository.getTokenByCode(code)
            tokenLocalRepository.token = token
            val profile = userRemoteRemoteRepository.getProfile()
            userLocalRepository.userProfile = profile
            return CompletedUseCase
        } catch (e: Exception) {
            tokenLocalRepository.token = null
            userLocalRepository.userProfile = null
            return ErrorUseCase(e.message, e)
        }
    }

}