package com.anton.github.domain.usecase

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginCallbackHandlerUseCaseTest {

    lateinit var usecase: LoginCallbackHandlerUseCase

    @Before
    fun setup() {
        usecase = LoginCallbackHandlerUseCaseImpl()
    }

    @Test
    fun handleCodeError() {
        val result = usecase.handle("http://base.url?error=some_error")
        assertTrue(result is ErrorUseCase)
    }

    @Test
    fun handleCodeSuccess() {
        val result = usecase.handle("https://base.url?dummy_key=key&code=4567")
        assertTrue(result is SuccessUseCase)
        assertEquals("4567", (result as SuccessUseCase<String>).result)
    }

    @Test
    fun handleWrongUrl() {
        val result = usecase.handle("wrong_url")
        assertTrue(result is ErrorUseCase)
    }
}