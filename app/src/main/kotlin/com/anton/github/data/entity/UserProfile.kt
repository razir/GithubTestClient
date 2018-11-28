package com.anton.github.data.entity

import com.google.gson.annotations.SerializedName

class UserProfile {
    val id: String? = null
    val login: String? = null
    val name: String? = null
    val url: String? = null
    @SerializedName("avatar_url")
    val avatarUrl: String? = null
    val followers: Int = 0;
    val following: Int = 0;
    val email: String? = null
}