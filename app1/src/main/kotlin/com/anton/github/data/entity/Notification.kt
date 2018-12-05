package com.anton.github.data.entity

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Repository::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("repository_id")
        )
    )
)
class Notification {
    @PrimaryKey
    var id: String = ""
    var reason: String? = null
    var unread: Boolean = false
    var url: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    @Ignore
    var repository: Repository? = null
    @Embedded
    var subject: Subject? = null
    @ColumnInfo(name = "repository_id")
    var repositoryId: String = ""
}

const val SUBJECT_TYPE_PULL_REQUEST = "PullRequest"
const val SUBJECT_TYPE_ALERT = "RepositoryVulnerabilityAlert"
const val SUBJECT_TYPE_ISSUE = "Issue"

class Subject {
    var title: String? = null
    @ColumnInfo(name = "subject_url")
    var url: String? = null
    var type: String? = null
}
