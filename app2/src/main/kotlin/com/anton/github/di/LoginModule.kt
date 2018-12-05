package com.anton.github.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.anton.github.datasource.auth.GithubAuthLocalRepository
import com.anton.github.domain.usecase.AuthorizeUseCase
import com.anton.github.domain.usecase.LoginCallbackHandlerUseCase
import com.anton.github.domain.usecase.LoginUrlComposerUseCase
import com.anton.github.presentation.login.LoginActivity
import com.anton.github.presentation.login.start.NotLoggedFragment
import com.anton.github.presentation.login.web.LoginWebViewFragment
import com.anton.github.presentation.login.web.LoginWebViewModel
import com.anton.github.presentation.splash.SplashActivity
import com.anton.github.presentation.splash.SplashViewModel
import com.anton.github.utils.NetworkStatusProvider
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = arrayOf(
        LoginModule.ProvideViewModel::class
    )
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