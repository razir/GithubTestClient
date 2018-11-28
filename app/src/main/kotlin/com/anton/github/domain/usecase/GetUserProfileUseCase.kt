package com.anton.github.domain.usecase

import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserRemoteRepository

interface GetRemoteUserProfileUseCase : BaseUseCase<UserProfile>

class GetRemoteUserProfileUseCaseImpl(
    private val userRemoteRepository: UserRemoteRepository,
    private val userLocalRepository: UserLocalRepository
) : GetRemoteUserProfileUseCase {

    override suspend fun run(): ResultUseCase<UserProfile> {
        try {
            val remoteProfile = userRemoteRepository.getProfile()
            userLocalRepository.userProfile = remoteProfile
            return SuccessUseCase(remoteProfile)
        } catch (e: Exception) {
            return ErrorUseCase(e = e)
        }
    }
}

