package com.anton.github.data.entity

import com.google.gson.annotations.SerializedName

class UserProfile {
    var id: String? = null
    var login: String? = null
    var name: String? = null
    var url: String? = null
    @SerializedName("avatar_url")
    var avatarUrl: String? = null
    var followers: Int = 0;
    var following: Int = 0;
    var email: String? = null
}