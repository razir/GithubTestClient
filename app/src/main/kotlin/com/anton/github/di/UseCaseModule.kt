package com.anton.github.di

import com.anton.github.domain.usecase.*
import org.koin.dsl.module.module

val useCaseModule = module {
    factory<LoginUrlComposerUseCase> { LoginUrlComposerUseCaseImpl() }

    factory<LoginCallbackHandlerUseCase> { LoginCallbackHandlerUseCaseImpl() }

    factory<AuthorizeUseCase> { AuthorizeUseCaseImpl(get(), get(), get(), get()) }

    factory<GetRemoteNotificationsUseCase> { GetRemoteNotificationsUseCaseImpl(get(), get()) }

    factory<GetRemoteUserProfileUseCase> { GetRemoteUserProfileUseCaseImpl(get(), get()) }

    factory<GetLocalNotificationsUseCase> { GetLocalNotificationsUseCaseImpl(get()) }

    factory<GetDetailsUrlUseCase> { GetDetailsUseUrlCaseImpl(get()) }

    factory<LogoutUseCase> { LogoutUseCaseImpl(get(), get(), get()) }
}