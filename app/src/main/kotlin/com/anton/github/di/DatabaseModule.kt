package com.anton.github.di

import android.arch.persistence.room.Room
import com.anton.github.datasource.AppDatabase
import org.koin.dsl.module.module


val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "github_db"
        ).build()
    }

    single { get<AppDatabase>().notificationDao() }

    single { get<AppDatabase>().repositoryDao() }

}