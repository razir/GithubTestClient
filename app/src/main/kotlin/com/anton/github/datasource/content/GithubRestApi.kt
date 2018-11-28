package com.anton.github.datasource.content

import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.UserProfile
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRestApi {

    @GET("user")
    fun getUserProfile(): Deferred<UserProfile>

    @GET("notifications")
    fun getUserNotifications(@Query("page") page: Int): Deferred<List<Notification>>
}