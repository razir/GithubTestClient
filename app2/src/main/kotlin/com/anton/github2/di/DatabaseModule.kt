package com.anton.github2.di

import android.arch.persistence.room.Room
import android.content.Context
import com.anton.github2.datasource.AppDatabase
import com.anton.github2.datasource.content.notifications.NotificationsDao
import com.anton.github2.datasource.content.notifications.RepositoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDB(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "github_db"
        ).build()
    }

    @Provides
    fun provideNotificationDao(appDatabase: AppDatabase): NotificationsDao {
        return appDatabase.notificationDao()
    }

    @Provides
    fun provideRepositoryDAO(appDatabase: AppDatabase): RepositoryDao {
        return appDatabase.repositoryDao()
    }
}