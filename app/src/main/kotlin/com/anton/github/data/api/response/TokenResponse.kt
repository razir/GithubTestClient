package com.anton.github.data.api.response

import com.google.gson.annotations.SerializedName

class TokenResponse {
    @SerializedName("token_type")
    val tokenType: String? = null

    @SerializedName("access_token")
    val accessToken: String? = null

    @SerializedName("scope")
    val scope: String? = null
}