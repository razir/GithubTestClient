package com.anton.medium.domain.usecase

import android.net.Uri


interface LoginCallbackHandlerUseCase {

    fun handle(url: String): ResultUseCase
}

class LoginCallbackHandlerUseCaseImpl : LoginCallbackHandlerUseCase {

    override fun handle(url: String): ResultUseCase {
        val uri = Uri.parse(url)
        val error = uri.getQueryParameter("error")
        if (error != null) {
            return ErrorUseCase(msg = error)
        } else {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                return SuccessUseCase(code)
            }
        }
        return ErrorUseCase()
    }
}