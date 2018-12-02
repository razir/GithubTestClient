package com.anton.github.extensions

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.anton.github.R
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DateExtensionsTest {

    @Test
    fun parseDate() {
        val date1 = "2018-10-18T09:20:58Z".parseDate()
        val date2 = "2018.10.18T09.20.58Z".parseDate()

        val expectedDate1 = Calendar.getInstance().apply {
            set(2018, 9, 18, 9, 20, 58)
        }.time

        assertEquals(expectedDate1.toString(), date1.toString())
        assertTrue(date2 == null)
    }

    @Test
    fun nameDate() {
        val context = InstrumentationRegistry.getTargetContext()

        val today = Calendar.getInstance().time
        val todayName = today.name(context)
        assertEquals(context.getString(R.string.today), todayName)

        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }.time
        val yesterdayName = yesterday.name(context)
        assertEquals(context.getString(R.string.yesterday), yesterdayName)

        val daysAgo3 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -3)
        }.time

        val daysAgo3Name = daysAgo3.name(context)
        assertEquals(context.getString(R.string.days_past, 3), daysAgo3Name)
    }

}