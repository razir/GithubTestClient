package com.anton.github2.domain.usecase

import android.net.Uri
import io.reactivex.Single


interface LoginCallbackHandlerUseCase {

    fun handle(url: String): Single<String>
}

class LoginCallbackHandlerUseCaseImpl : LoginCallbackHandlerUseCase {

    override fun handle(url: String): Single<String> {
        return Single.defer {
            val uri = Uri.parse(url)
            val error = uri.getQueryParameter("error")
            if (error != null) {
                Single.error<String>(Throwable())
            } else {
                val code = uri.getQueryParameter("code")
                if (code != null) {
                    Single.just(code)
                }
            }
            Single.error<String>(Throwable())
        }
    }
}