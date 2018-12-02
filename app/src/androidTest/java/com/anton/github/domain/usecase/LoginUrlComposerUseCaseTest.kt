package com.anton.github.domain.usecase

import android.net.Uri
import android.support.test.runner.AndroidJUnit4
import com.anton.github.BuildConfig
import com.anton.github.constants.GITHUB_AUTHORIZE_URL
import com.anton.github.constants.GITHUB_OAUTH_REDIRECT_URL
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUrlComposerUseCaseTest {

    lateinit var usecase: LoginUrlComposerUseCase

    @Before
    fun setup() {
        usecase = LoginUrlComposerUseCaseImpl()
    }

    @Test
    fun compose() {
        val result = usecase.compose()
        val url = Uri.parse(result)
        assertTrue(result.startsWith(GITHUB_AUTHORIZE_URL))
        assertEquals(BuildConfig.GItHUB_CLIENT_ID, url.getQueryParameter("client_id"))
        assertEquals("notifications,user", url.getQueryParameter("scope"))
        assertEquals("dummy", url.getQueryParameter("state"))
        assertEquals("code", url.getQueryParameter("response_type"))
        assertEquals(GITHUB_OAUTH_REDIRECT_URL, url.getQueryParameter("redirect_uri"))
    }
}