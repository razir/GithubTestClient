package com.anton.medium.data.api.response

import com.google.gson.annotations.SerializedName

class TokenResponse {
    @SerializedName("token_type")
    val tokenType: String? = null

    @SerializedName("access_token")
    val accessToken: String? = null

    @SerializedName("refresh_token")
    val refreshToken: String? = null

    @SerializedName("scope")
    val scope: List<String>? = null

    @SerializedName("expires_at")
    val expiresAt: Long = 0

}