package com.anton.github.datasource.content.user

import android.content.Context
import com.anton.github.data.entity.UserProfile
import com.anton.github.extensions.json

interface UserLocalRepository {

    var userProfile: UserProfile?

}

private const val PREF_GROUP_USER = "user_data"
private const val PREF_PROFILE = "profile"

class UserLocalRepositoryImpl(context: Context) : UserLocalRepository {
    private val sharedPreferences = context.getSharedPreferences(PREF_GROUP_USER, Context.MODE_PRIVATE)

    override var userProfile: UserProfile? by json(sharedPreferences, PREF_PROFILE)
}