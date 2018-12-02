package com.anton.github.extensions

import android.support.test.runner.AndroidJUnit4
import com.anton.github.extensions.formatNumber
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NumberExtensionsTest {

    @Test
    fun testFormat() {
        val formatted1 = 10500.formatNumber()
        assertEquals("10 500", formatted1)

        val formatted2 = 50.formatNumber()
        assertEquals("50", formatted2)

        val formatted3 = 99913434.formatNumber()
        assertEquals("99 913 434", formatted3)
    }
}