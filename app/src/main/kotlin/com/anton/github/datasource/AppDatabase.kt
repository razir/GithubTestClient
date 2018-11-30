package com.anton.github.datasource

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.anton.github.data.entity.Notification
import com.anton.github.data.entity.Repository
import com.anton.github.datasource.content.notifications.NotificationsDao
import com.anton.github.datasource.content.notifications.RepositoryDao

@Database(entities = arrayOf(Notification::class, Repository::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationsDao

    abstract fun repositoryDao(): RepositoryDao
}