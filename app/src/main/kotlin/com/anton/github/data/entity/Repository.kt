package com.anton.github.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Notification::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("notification_id"),
            onDelete = CASCADE
        )
    )
)
class Repository {
    @PrimaryKey
    var id: String = ""
    @SerializedName("name")
    var name: String? = null
    @SerializedName("full_name")
    var fullName: String? = null
    @ColumnInfo(name = "notification_id")
    var notificationId: String? = null
}