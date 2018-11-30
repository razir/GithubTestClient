package com.anton.github.data.entity

import com.google.gson.annotations.SerializedName

class Notification {
    var id: String? = null
    var reason: String? = null
    var unread: Boolean = false
    var url: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    var repository: Repository? = null
    var subject: Subject? = null
}

const val SUBJECT_TYPE_PULL_REQUEST = "PullRequest"
const val SUBJECT_TYPE_ALERT = "RepositoryVulnerabilityAlert"
const val SUBJECT_TYPE_ISSUE = "Issue"

class Subject {
    val title: String? = null
    val url: String? = null
    val type: String? = null
}
