package com.anton.github2.data.api.response

import com.google.gson.annotations.SerializedName

class TokenResponse {
    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("scope")
    var scope: String? = null
}