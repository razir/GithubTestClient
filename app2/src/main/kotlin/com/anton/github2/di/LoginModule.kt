package com.anton.github2.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.anton.github2.domain.usecase.AuthorizeUseCase
import com.anton.github2.domain.usecase.LoginCallbackHandlerUseCase
import com.anton.github2.domain.usecase.LoginUrlComposerUseCase
import com.anton.github2.presentation.login.LoginActivity
import com.anton.github2.presentation.login.start.NotLoggedFragment
import com.anton.github2.presentation.login.web.LoginWebViewFragment
import com.anton.github2.presentation.login.web.LoginWebViewModel
import com.anton.github2.utils.NetworkStatusProvider
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        LoginModule.ProvideViewModel::class
    ]
)
abstract class LoginModule {

    @ContributesAndroidInjector
    abstract fun bindActivity(): LoginActivity

    @ContributesAndroidInjector(
        modules = [
            InjectViewModel::class
        ]
    )
    abstract fun bindLoginWeb(): LoginWebViewFragment

    @ContributesAndroidInjector()
    abstract fun bindNotLogged(): NotLoggedFragment


    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(LoginWebViewModel::class)
        fun provideLoginViewModel(
            loginUrlComposerUseCase: LoginUrlComposerUseCase,
            loginUrlCallbackHandlerUseCase: LoginCallbackHandlerUseCase,
            authorizeUseCase: AuthorizeUseCase,
            networkStatusProvider: NetworkStatusProvider
        ): ViewModel =
            LoginWebViewModel(
                loginUrlComposerUseCase,
                loginUrlCallbackHandlerUseCase,
                authorizeUseCase,
                networkStatusProvider
            )
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideLoginViewModel(
            factory: ViewModelProvider.Factory,
            target: LoginWebViewFragment
        ): LoginWebViewModel =
            ViewModelProviders.of(target, factory).get(LoginWebViewModel::class.java)
    }
}