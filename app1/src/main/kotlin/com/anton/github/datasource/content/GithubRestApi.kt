package com.anton.github.datasource.content

import com.anton.github.data.api.response.DetailsResponse
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.UserProfile
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GithubRestApi {

    @GET("user")
    fun getUserProfile(): Deferred<UserProfile>

    @GET("notifications")
    fun getUserNotifications(@Query("page") page: Int): Deferred<List<Notification>>

    @GET
    fun getDetails(@Url url: String): Deferred<DetailsResponse>
}