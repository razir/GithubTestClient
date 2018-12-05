package com.anton.github2.datasource

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.anton.github2.data.entity.Notification
import com.anton.github2.data.entity.Repository
import com.anton.github2.datasource.content.notifications.NotificationsDao
import com.anton.github2.datasource.content.notifications.RepositoryDao

@Database(entities = arrayOf(Notification::class, Repository::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationsDao

    abstract fun repositoryDao(): RepositoryDao
}