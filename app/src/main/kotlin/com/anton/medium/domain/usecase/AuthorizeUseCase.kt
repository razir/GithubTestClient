package com.anton.medium.domain.usecase

import com.anton.medium.datasource.auth.MediumAuthLocalRepository
import com.anton.medium.datasource.auth.MediumAuthRemoteRepository
import com.anton.medium.datasource.user.MediumUserLocalRepository
import com.anton.medium.datasource.user.MediumUserRemoteRepository

interface AuthorizeUseCase {
    suspend fun run(code: String): ResultUseCase
}

class AuthorizeUseCaseImpl(
    private val tokenRemoteRepository: MediumAuthRemoteRepository,
    private val tokenLocalRepository: MediumAuthLocalRepository,
    private val userLocalRepository: MediumUserLocalRepository,
    private val userRemoteRemoteRepository: MediumUserRemoteRepository
) : AuthorizeUseCase {

    override suspend fun run(code: String): ResultUseCase {
        try {
            val token = tokenRemoteRepository.getTokenByCode(code)
            tokenLocalRepository.token = token
            val profile = userRemoteRemoteRepository.getProfile()
            profile?.let {
                userLocalRepository.setProfile(it)
            }
            return CompletedUseCase
        } catch (e: Exception) {
            return ErrorUseCase(e.message, e)
        }
    }

}