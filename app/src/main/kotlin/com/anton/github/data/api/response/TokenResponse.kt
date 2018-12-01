package com.anton.github.data.api.response

import com.google.gson.annotations.SerializedName

class TokenResponse {
    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("scope")
    var scope: String? = null
}