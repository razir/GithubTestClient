package com.anton.github.domain.usecase

import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserRemoteRepository
import io.reactivex.Single

interface GetRemoteUserProfileUseCase : BaseUseCase<UserProfile>

class GetRemoteUserProfileUseCaseImpl(
    private val userRemoteRepository: UserRemoteRepository,
    private val userLocalRepository: UserLocalRepository
) : GetRemoteUserProfileUseCase {

    override fun run(): Single<UserProfile> {
        return userRemoteRepository.getProfile()
            .doOnSuccess {
                userLocalRepository.userProfile = it
            }
    }
}

