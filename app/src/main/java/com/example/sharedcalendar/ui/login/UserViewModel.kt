package com.example.sharedcalendar.ui.login

import android.text.Editable
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * ViewModel for the login screen.
 * @constructor Creates a new instance of [UserViewModel].
 */
class UserViewModel : ViewModel() {
    fun valuesAreEqual(
        value1: Editable,
        value2: Editable,
    ): Boolean {
        return value1.toString() == value2.toString()
    }

    fun isUsernameValid(username: Editable): Boolean {
        return username.toString().isNotBlank() && !username.toString().contains(' ')
    }

    /**
     * Email Validation: Checks if the email is valid.
     * @param email The email to be checked.
     * @return True if the email is valid, false otherwise.
     */
    fun isEmailValid(email: Editable): Boolean {
        return if (email.toString().contains('@')) {
            PatternsCompat.EMAIL_ADDRESS.matcher(email.toString()).matches()
        } else {
            email.toString().isNotBlank()
        }
    }

    /**
     * Password Strength: Checks if the password is strong.
     * @param password The password to be checked.
     * @return True if the password is strong, false otherwise.
     */
    fun isPasswordStrong(password: Editable): Boolean {
        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password.toString())

        return matcher.matches()
    }

    /**
     * Password Validation: Checks if the password is valid.
     * @param password The password to be checked.
     * @return True if the password is valid, false otherwise.
     */
    fun isPasswordValid(password: Editable): Boolean {
        return password.toString().isNotBlank() && isPasswordStrong(password)
    }
}
