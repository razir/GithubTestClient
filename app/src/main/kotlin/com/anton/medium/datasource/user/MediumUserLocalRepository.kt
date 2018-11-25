package com.anton.medium.datasource.user

import android.content.Context
import com.anton.medium.data.entity.UserProfile
import com.anton.medium.extensions.json

interface MediumUserLocalRepository {

    suspend fun getProfile(): UserProfile?
    suspend fun setProfile(userProfile: UserProfile)
}

private const val PREF_GROUP_USER = "user_data"
private const val PREF_PROFILE = "profile"

class MediumUserLocalRepositoryImpl(context: Context) : MediumUserLocalRepository {
    private val sharedPreferences = context.getSharedPreferences(PREF_GROUP_USER, Context.MODE_PRIVATE)
    private var userProfile: UserProfile? by json(sharedPreferences, PREF_PROFILE)

    override suspend fun getProfile(): UserProfile? = userProfile

    override suspend fun setProfile(userProfile: UserProfile) {
        this.userProfile = userProfile
    }

}