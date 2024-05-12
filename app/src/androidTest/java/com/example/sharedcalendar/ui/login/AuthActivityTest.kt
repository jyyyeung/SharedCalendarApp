package com.example.sharedcalendar.ui.login

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class AuthActivityTest {

    @Test
    fun onStart() {
    }

    @Test
    fun onCreate() {
    }

    @Test
    fun startAuthActivity() {
        val authActivity = spyk(AuthActivity())
        every { authActivity.startActivity(any()) } returns Unit
    }
}