package com.example.sharedcalendar.ui.login

import androidx.lifecycle.ViewModel
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginViewModelFactoryTest {

    private lateinit var factory: LoginViewModelFactory

    @BeforeEach
    fun setup() {
        factory = LoginViewModelFactory()
    }

    @Test
    fun `creates UserViewModel when requested`() {
        val viewModel = factory.create(UserViewModel::class.java)
        assertTrue(viewModel is UserViewModel)
    }

    @Test
    fun `throws IllegalArgumentException when unknown ViewModel class is requested`() {
        try {
            factory.create(OtherViewModel::class.java)
            fail("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Unknown ViewModel class"))
        }
    }
}

class OtherViewModel : ViewModel()