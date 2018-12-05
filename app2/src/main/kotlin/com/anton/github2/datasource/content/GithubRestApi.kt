package com.anton.github2.datasource.content

import com.anton.github2.data.api.response.DetailsResponse
import com.anton.github2.data.entity.Notification
import com.anton.github2.data.entity.UserProfile
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GithubRestApi {

    @GET("user")
    fun getUserProfile(): Single<UserProfile>

    @GET("notifications")
    fun getUserNotifications(@Query("page") page: Int): Single<List<Notification>>

    @GET
    fun getDetails(@Url url: String): Single<DetailsResponse>
}