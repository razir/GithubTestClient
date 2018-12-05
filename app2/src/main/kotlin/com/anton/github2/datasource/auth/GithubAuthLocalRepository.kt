package com.anton.github2.datasource.auth

import android.content.Context
import com.anton.github2.data.api.response.TokenResponse
import com.anton.github2.extensions.json

interface GithubAuthLocalRepository {
    var token: TokenResponse?
}

private const val PREF_GROUP_TOKEN = "tokens"
private const val PREF_TOKEN = "token"

class GithubAuthLocalRepositoryImpl(context: Context) : GithubAuthLocalRepository {

    private val sharedPreferences = context.getSharedPreferences(PREF_GROUP_TOKEN, Context.MODE_PRIVATE)
    override var token: TokenResponse? by json(sharedPreferences, PREF_TOKEN)

}