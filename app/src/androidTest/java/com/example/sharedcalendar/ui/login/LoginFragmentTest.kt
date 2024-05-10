package com.example.sharedcalendar.ui.login


import androidx.fragment.app.testing.FragmentScenario.Companion.launchInContainer
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.sharedcalendar.R
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class LoginFragmentTest {

    private lateinit var mockAuth: FirebaseAuth

    @BeforeEach
    fun setUp() {
        mockkStatic(FirebaseAuth::class)
        mockAuth = mockk(relaxed = true)

        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
    }

    private val validEmail = "test10@example.com"
    private val validPassword = "password"

    @Test
    fun test_whenLoginButtonIsClickedAndCredentialsAreValid_UserIsLoggedIn() {
        // Given
        every { mockAuth.signInWithEmailAndPassword(any(), any()) } returns mockk(relaxed = true)


//        val args = bundleOf("auth" to mockAuth)
        // Launch Fragment
        launchInContainer(LoginFragment::class.java, null, R.style.Base_Theme_SharedCalendar, null)
//        launchFragmentInContainer<LoginFragment>()

        // When
        onView(withId(R.id.etUserEmail)).perform(click())
        onView(withId(R.id.etUserEmail)).perform(typeText(validEmail))
        onView(withId(R.id.etLoginPassword)).perform(click())
        onView(withId(R.id.etLoginPassword)).perform(typeText(validPassword))
        onView(withId(R.id.btnLogin)).perform(click())

        // Then
        verify { mockAuth.signInWithEmailAndPassword(any(), any()) }
    }

    @Test
    fun whenLoginButtonIsClickedAndCredentialsAreInvalid_errorIsShown() {
        // Given
        every { mockAuth.signInWithEmailAndPassword(any(), any()) } returns mockk(relaxed = true)

        // Launch Fragment
        launchFragmentInContainer<LoginFragment>()

        // When
        onView(withId(R.id.btnLogin)).perform(click())

        // Then
        verify(exactly = 0) { mockAuth.signInWithEmailAndPassword(any(), any()) }
    }
}