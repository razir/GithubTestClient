package com.anton.medium.datasource.auth

import android.content.Context
import com.anton.medium.data.api.response.TokenResponse
import com.anton.medium.extensions.json

interface MediumAuthLocalRepository {
    var token: TokenResponse?
}

private const val PREF_GROUP_TOKEN = "tokens"
private const val PREF_TOKEN = "token"

class MediumAuthLocalRepositoryImpl(context: Context) : MediumAuthLocalRepository {

    private val sharedPreferences = context.getSharedPreferences(PREF_GROUP_TOKEN, Context.MODE_PRIVATE)
    override var token: TokenResponse? by json(sharedPreferences, PREF_TOKEN)

}