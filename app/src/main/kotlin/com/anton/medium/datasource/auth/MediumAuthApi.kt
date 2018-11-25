package com.anton.medium.datasource.auth

import com.anton.medium.constants.AUTH_MEDIUM_REDIRECT_URL
import com.anton.medium.data.api.response.TokenResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MediumAuthApi {

    @POST("tokens")
    @FormUrlEncoded
    fun getTokenByCode(
        @Field("code") code: String, @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String, @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String = AUTH_MEDIUM_REDIRECT_URL
    ): Deferred<TokenResponse>

    @POST("tokens")
    @FormUrlEncoded
    fun getTokenByRefreshToken(
        @Field("refresh_token") refreshToken: String, @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String, @Field("grant_type") grantType: String = "refresh_token"
    ): Deferred<TokenResponse>
}