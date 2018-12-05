package com.anton.github.datasource.auth

import com.anton.github.constants.GITHUB_OAUTH_REDIRECT_URL
import com.anton.github.data.api.response.TokenResponse
import io.reactivex.Single
import retrofit2.http.*

interface GithubAuthApi {


    @POST("oauth/access_token")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    fun getTokenByCode(
        @Field("code") code: String, @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String = GITHUB_OAUTH_REDIRECT_URL
    ): Single<TokenResponse>
}