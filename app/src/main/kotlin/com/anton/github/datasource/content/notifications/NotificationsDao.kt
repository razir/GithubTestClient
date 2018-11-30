package com.anton.github.datasource.content.notifications

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.Repository

@Dao
interface NotificationsDao {
    @Query("SELECT * FROM notification")
    fun getAll(): List<Notification>

    @Insert
    fun save(notifications: List<Notification>)

    @Query("DELETE FROM notification")
    fun deleteAll()
}

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repository WHERE notification_id == :notificationId ")
    fun getForNotificationId(notificationId: String): Repository

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(repository: Repository)

    @Query("DELETE FROM repository")
    fun deleteAll()
}