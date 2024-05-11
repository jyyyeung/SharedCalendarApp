package com.example.sharedcalendar

import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HexToJetpackColorTest {

    @Before
    fun setUp() {
        mockkStatic(android.graphics.Color::class)

    }

    @Test
    fun getColor() {
        val color = HexToJetpackColor.getColor("#FF0000")
        assertEquals(color.red, 1f)
        assertEquals(color.green, 0f)
        assertEquals(color.blue, 0f)
        assertEquals(color.alpha, 1f)
    }
}