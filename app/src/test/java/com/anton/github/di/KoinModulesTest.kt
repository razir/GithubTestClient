package com.anton.github.di

import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.test.KoinTest
import org.koin.test.checkModules

class KoinModulesTest : KoinTest {

    @Test
    fun testDI() {
        val mockedAndroidContext = module {
            single { mock<Context>() }
        }
        val modulesList = listOf(
            apiModule, databaseModule, repositoryModule, useCaseModule, viewModelModule,
            mockedAndroidContext
        )
        checkModules(modulesList)
    }
}