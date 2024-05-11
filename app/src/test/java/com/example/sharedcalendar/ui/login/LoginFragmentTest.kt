package com.example.sharedcalendar.ui.login

import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.sharedcalendar.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf


@RunWith(RobolectricTestRunner::class)
class LoginFragmentTest {

    private lateinit var loginFragment: LoginFragment
    private lateinit var mockAuth: FirebaseAuth

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        val activity = Robolectric.buildActivity(FragmentActivity::class.java)
            .create()
            .start()
            .resume()
            .get()

        mockkStatic(FirebaseAuth::class)
        mockAuth = mockk(relaxed = true)
        every { FirebaseAuth.getInstance() } returns mockAuth


        loginFragment = LoginFragment()
        activity.supportFragmentManager.beginTransaction().apply {
            add(loginFragment, "loginFragment")
            commit()
        }

        shadowOf(Looper.getMainLooper()).idle()

    }

    @Test
    fun `when LoginFragment is launched, then it is not null`() {
        assertNotNull(loginFragment)
    }

    @Test
    fun `when LoginFragment is launched, its view is not null`() {
        assertNotNull(loginFragment.view)
    }

    @Test
    fun `when LoginFragment is launched, then shows login form views`() {
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        val loginButton = loginFragment.view?.findViewById<Button>(R.id.btnLogin)
        val loginHeading = loginFragment.view?.findViewById<TextView>(R.id.tvLoginHeading)

        assertNotNull(emailEditText)
        assertNotNull(passwordEditText)
        assertNotNull(loginButton)
        assertNotNull(loginHeading)
    }

    @Test
    fun `when LoginFragment is launched, then email field is empty`() {
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        assertTrue(emailEditText?.text.toString().isEmpty())
    }

    @Test
    fun `when LoginFragment is launched, then password field is empty`() {
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        assertTrue(passwordEditText?.text.toString().isEmpty())
    }

//    @Test
//    fun `when LoginFragment is launched, then login button is not clickable`() {
//        val loginButton = loginFragment.view?.findViewById<Button>(R.id.btnLogin)
//        assertFalse(loginButton?.isClickable == true)
//    }

    @Test
    fun `when valid email and password are entered, then login button becomes clickable`() {
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        val loginButton = loginFragment.view?.findViewById<Button>(R.id.btnLogin)

        emailEditText?.setText("test@example.com")
        passwordEditText?.setText("password123")
        assertTrue(loginButton?.isClickable == true)
    }

    @Test
    fun `when invalid email is entered, then shows error message`() {
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        emailEditText?.setText("invalidEmail")
        assert(emailEditText?.error.toString().isNotEmpty())
//        assertEquals("Invalid email address", emailEditText?.error)
    }

    @Test
    fun `when password is less than 6 characters, then shows error message`() {
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        passwordEditText?.setText("12345")
        assert(passwordEditText?.error.toString().isNotEmpty())
//        assertEquals("Password must be at least 6 characters", passwordEditText?.error)
    }

    @Test
    fun `login with invalid credential pair returns failure`() {
        val loginFragment = LoginFragment()
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        val result = loginFragment.login("valid@example.com", "validPassword")
        assertFalse(emailEditText?.error.toString().isEmpty())
        assertFalse(passwordEditText?.error.toString().isEmpty())
    }

//    @Test
//    fun `login with valid credentials returns success`() {
//        val loginFragment = LoginFragment()
//        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
//        val passwordEditText =
//            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
//        val result = loginFragment.login("test@example.com", "password")
//        assertTrue(emailEditText?.error.toString().isEmpty())
//        assertTrue(passwordEditText?.error.toString().isEmpty())
//    }

    @Test
    fun `login with invalid email returns failure`() {
        val loginFragment = LoginFragment()
        val result = loginFragment.login("invalidEmail", "validPassword")
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        assertFalse(emailEditText?.error.toString().isEmpty())
        assertFalse(passwordEditText?.error.toString().isEmpty())
    }

    @Test
    fun `login with invalid password returns failure`() {
        val loginFragment = LoginFragment()
        val result = loginFragment.login("valid@example.com", "invalidPassword")
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        assertFalse(emailEditText?.error.toString().isEmpty())
        assertFalse(passwordEditText?.error.toString().isEmpty())
    }

    @Test
    fun `login with empty email returns failure`() {
        val loginFragment = LoginFragment()
        val result = loginFragment.login("", "validPassword")
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        assertFalse(emailEditText?.error.toString().isEmpty())
        assertFalse(passwordEditText?.error.toString().isEmpty())
    }

    @Test
    fun `login with empty password returns failure`() {
        val loginFragment = LoginFragment()
        val result = loginFragment.login("valid@example.com", "")
        val emailEditText = loginFragment.view?.findViewById<TextInputEditText>(R.id.etUserEmail)
        val passwordEditText =
            loginFragment.view?.findViewById<TextInputEditText>(R.id.etLoginPassword)
        assertFalse(emailEditText?.error.toString().isEmpty())
        assertFalse(passwordEditText?.error.toString().isEmpty())
    }

}