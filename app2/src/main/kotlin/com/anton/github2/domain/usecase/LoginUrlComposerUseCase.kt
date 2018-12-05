package com.anton.github2.domain.usecase

import android.net.Uri
import com.anton.github2.BuildConfig
import com.anton.github2.constants.GITHUB_AUTHORIZE_URL
import com.anton.github2.constants.GITHUB_OAUTH_REDIRECT_URL

interface LoginUrlComposerUseCase {

    fun compose(): String
}

private const val LOGIN_SCOPE = "notifications,user"

class LoginUrlComposerUseCaseImpl : LoginUrlComposerUseCase {

    override fun compose(): String {
        return Uri.parse(GITHUB_AUTHORIZE_URL).buildUpon()
            .appendQueryParameter("client_id", BuildConfig.GItHUB_CLIENT_ID)
            .appendQueryParameter("scope", LOGIN_SCOPE)
            .appendQueryParameter("state", "dummy")
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", GITHUB_OAUTH_REDIRECT_URL)
            .build().toString()
    }

}