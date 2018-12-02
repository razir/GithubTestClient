package com.anton.github.datasource

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.anton.github.data.entity.UserProfile
import com.anton.github.datasource.content.user.UserLocalRepository
import com.anton.github.datasource.content.user.UserLocalRepositoryImpl
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserLocalRepositoryTest {

    lateinit var userLocalRepository: UserLocalRepository

    @Before
    fun setup() {
        userLocalRepository = UserLocalRepositoryImpl(InstrumentationRegistry.getTargetContext())
    }

    @Test
    fun setUser() {
        val testUser = UserProfile().apply {
            name = "test name"
            id = "some_id"
            login = "test_login"
            url = "test_url"
            avatarUrl = "avatar_url"
            followers = 10
            following = 20
        }
        userLocalRepository.userProfile = testUser
        val savedUser = userLocalRepository.userProfile
        assertTrue(usersEquals(testUser, savedUser))
    }

    @Test
    fun clearUser() {
        val testUser = UserProfile().apply {
            name = "test name"
            id = "some_id"
        }
        userLocalRepository.userProfile = testUser
        userLocalRepository.userProfile = null
        val savedUser = userLocalRepository.userProfile
        assertTrue(savedUser == null)
    }

    private fun usersEquals(user1: UserProfile?, user2: UserProfile?): Boolean {
        if (user1 === user2) return true
        if (user1?.javaClass != user2?.javaClass) return false

        if (user1?.id != user2?.id) return false
        if (user1?.login != user2?.login) return false
        if (user1?.name != user2?.name) return false
        if (user1?.url != user2?.url) return false
        if (user1?.avatarUrl != user2?.avatarUrl) return false
        if (user1?.followers != user2?.followers) return false
        if (user1?.following != user2?.following) return false
        if (user1?.email != user2?.email) return false

        return true
    }
}