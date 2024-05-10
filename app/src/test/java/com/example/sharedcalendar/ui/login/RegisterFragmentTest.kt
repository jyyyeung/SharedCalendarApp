package com.example.sharedcalendar.ui.login

import android.os.Looper
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.example.sharedcalendar.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class RegisterFragmentTest {
    private lateinit var registerFragment: RegisterFragment
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


        registerFragment = RegisterFragment()
        activity.supportFragmentManager.beginTransaction().apply {
            add(registerFragment, "registerFragment")
            commit()
        }

        Shadows.shadowOf(Looper.getMainLooper()).idle()

    }

    @Test
    fun `when RegisterFragment is launched, then it is not null`() {
        assertNotNull(registerFragment)
    }

    @Test
    fun `when RegisterFragment is launched, its view is not null`() {
        assertNotNull(registerFragment.view)
    }

    @Test
    fun `when RegisterFragment is launched, then shows register form views`() {
        val usernameField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterUsername)
        val emailField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterEmail)
        val passwordField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterPassword)
        val confirmPasswordField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterConfirmPassword)


        assertNotNull(usernameField)
        assertNotNull(emailField)
        assertNotNull(passwordField)
        assertNotNull(confirmPasswordField)
    }

    @Test
    fun `when RegisterFragment is launched, then shows register button`() {
        val registerButton = registerFragment.view?.findViewById<Button>(R.id.btn_register)
        assertNotNull(registerButton)
    }

    @Test
    fun `when RegisterFragment is launched, then username field is empty`() {
        val usernameField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterUsername)
        assert(usernameField?.text.toString().isEmpty())
    }

    @Test
    fun `when RegisterFragment is launched, then email field is empty`() {
        val emailField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterEmail)
        assert(emailField?.text.toString().isEmpty())
    }

    @Test
    fun `when RegisterFragment is launched, then password field is empty`() {
        val passwordField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterPassword)
        assert(passwordField?.text.toString().isEmpty())
    }

    @Test
    fun `when RegisterFragment is launched, then confirm password field is empty`() {
        val confirmPasswordField =
            registerFragment.view?.findViewById<TextInputEditText>(R.id.etRegisterConfirmPassword)
        assert(confirmPasswordField?.text.toString().isEmpty())
    }
    // TODO: Add more tests


    @Test
    fun `register with valid credentials returns success`() {
        // TODO: Implement test
    }

    @Test
    fun `register with invalid email returns error`() {
        // TODO: Implement test
    }

    @Test
    fun `register with short password returns error`() {
        // TODO: Implement test
    }

    @Test
    fun `register with mismatched password and confirmation returns error`() {
        // TODO: Implement test
    }

    @Test
    fun `register with already registered email returns error`() {
        // TODO: Implement test
    }
}