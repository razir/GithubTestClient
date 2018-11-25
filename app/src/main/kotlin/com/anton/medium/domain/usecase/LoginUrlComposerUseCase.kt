package com.anton.medium.domain.usecase

import android.net.Uri
import com.anton.medium.BuildConfig
import com.anton.medium.constants.AUTH_MEDIUM_REDIRECT_URL
import com.anton.medium.constants.BASE_MEDIUM_AUTH_URL

interface LoginUrlComposerUseCase {

    fun compose(): String
}

private const val LOGIN_SCOPE = "basicProfile,listPublications,publishPost"

class LoginUrlComposerUseCaseImpl : LoginUrlComposerUseCase {

    override fun compose(): String {
        return Uri.parse(BASE_MEDIUM_AUTH_URL).buildUpon()
            .appendQueryParameter("client_id", BuildConfig.MEDIUM_CLIENT_ID)
            .appendQueryParameter("scope", LOGIN_SCOPE)
            .appendQueryParameter("state", "dummy")
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", AUTH_MEDIUM_REDIRECT_URL)
            .build().toString()
    }

}