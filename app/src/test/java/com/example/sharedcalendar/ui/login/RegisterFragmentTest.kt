package com.example.sharedcalendar.ui.login

import android.os.Looper
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.test.espresso.matcher.PreferenceMatchers
import com.example.sharedcalendar.BaseTest
import com.example.sharedcalendar.MainActivity
import com.example.sharedcalendar.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class RegisterFragmentTest : BaseTest() {
    private lateinit var registerFragment: RegisterFragment

    override fun extendedSetup() {
        MockKAnnotations.init(this)
        val activity =
            Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()

        registerFragment = RegisterFragment()
        activity.supportFragmentManager.beginTransaction().apply {
            add(registerFragment, "registerFragment")
            commit()
        }

        Shadows.shadowOf(Looper.getMainLooper()).idle()

        every {
            mockAuth.createUserWithEmailAndPassword(
                testEmail, testPassword
            )
        }.answers { mockTaskAuthResult }
        val completionListenerCapture = slot<OnCompleteListener<AuthResult>>()
        every { mockTaskAuthResult.addOnCompleteListener(capture(completionListenerCapture)) } answers {
            completionListenerCapture.captured.onComplete(mockTaskAuthResult)
            mockTaskAuthResult
        }

        every { mockUser.updateProfile(any()) }.answers { mockTask }
        every { mockTask.isSuccessful }.returns(true)
        // 设置字段值
        setField(registerFragment, "auth", mockAuth)
//        setField(registerFragment, "sharedPrefs", mockSharedPrefs)
        setField(registerFragment, "db", mockFirestore)

    }


    @ExperimentalCoroutinesApi
    @Test
    fun testRegisterSuccess() {
        every { mockTaskAuthResult.isSuccessful }.returns(true)
        every { mockTask.isSuccessful }.returns(true)
        every { mockFirestore.collection("users").document(any()).set(any()) } answers {
            mockTask
        }
        val sharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(registerFragment.requireActivity())
        // 这里你可以调用你的函数，但由于它是私有的，我们需要通过反射来访问
        val function = registerFragment.javaClass.getDeclaredMethod(
            "register", String::class.java, String::class.java, String::class.java
        )
        function.isAccessible = true
        function.invoke(registerFragment, testEmail, testPassword, testUsername)


        verify { mockAuth.createUserWithEmailAndPassword(testEmail, testPassword) }
        assertTrue(mockTaskAuthResult.isSuccessful)
        verify { mockTaskAuthResult.addOnCompleteListener(any()) }
        assertEquals(sharedPrefs.getString("${testUserId}|name", null), testUsername)

        val intent =
            Shadows.shadowOf(registerFragment.requireActivity()).peekNextStartedActivity()
        assertEquals(intent.component?.className, MainActivity::class.java.canonicalName)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testRegisterFailure() {
        val sharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(registerFragment.requireActivity())
        every { mockTaskAuthResult.isSuccessful } returns false
        every { mockTaskAuthResult.exception} returns mockException

        val function = registerFragment.javaClass.getDeclaredMethod(
            "register", String::class.java, String::class.java, String::class.java
        )
        function.isAccessible = true
        function.invoke(registerFragment, testEmail, testPassword, testUsername)


        verify { mockAuth.createUserWithEmailAndPassword(testEmail, testPassword) }
//        assertTrue(mockTaskAuthResult.isSuccessful)
        verify { mockTaskAuthResult.addOnCompleteListener(any()) }
        assertEquals(sharedPrefs.getString("${testUserId}|name", null), null)

        val intent =
            Shadows.shadowOf(registerFragment.requireActivity()).peekNextStartedActivity()
        assertEquals(intent, null)
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