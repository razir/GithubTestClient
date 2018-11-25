package com.anton.medium.datasource.content

import com.anton.medium.data.api.response.DataResponse
import com.anton.medium.data.entity.UserProfile
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface MediumContentApi {

    @GET("me")
    fun getUserProfile(): Deferred<DataResponse<UserProfile>>
}