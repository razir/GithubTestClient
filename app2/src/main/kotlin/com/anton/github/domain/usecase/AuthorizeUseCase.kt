package com.anton.github.domain.usecase

import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.datasource.auth.GithubAuthRemoteRepository
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserRemoteRepository
import io.reactivex.Completable

interface AuthorizeUseCase {
    fun run(code: String): Completable
}

class AuthorizeUseCaseImpl(
    private val tokenRemoteRepository: GithubAuthRemoteRepository,
    private val tokenLocalRepository: GithubAuthLocalRepository,
    private val userLocalRepository: UserLocalRepository,
    private val userRemoteRemoteRepository: UserRemoteRepository
) : AuthorizeUseCase {

    override fun run(code: String): Completable {
        return tokenRemoteRepository.getTokenByCode(code)
            .doOnSuccess { token ->
                tokenLocalRepository.token = token
            }.flatMap {
                userRemoteRemoteRepository.getProfile()
            }.doOnSuccess { profile ->
                userLocalRepository.userProfile = profile
            }.doOnError {
                tokenLocalRepository.token = null
                userLocalRepository.userProfile = null
            }.toCompletable()
    }

}