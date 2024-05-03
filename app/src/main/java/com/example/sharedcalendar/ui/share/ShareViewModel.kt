package com.example.sharedcalendar.ui.share

import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.data.UserRepository

/**
 * ViewModel for the login screen.
 * @constructor Creates a new instance of [ShareViewModel].
 * @see UserRepository
 */
class ShareViewModel : ViewModel() {

    companion object {
        private val TAG: String = ShareViewModel::class.java.name
    }


    private fun valueIsNotEmpty(value: Editable): Boolean {
        return value.isNotBlank()
    }

    fun valuesAreEqual(value1: Editable, value2: Editable): Boolean {
        return value1.toString() == value2.toString()
    }

    fun isUsernameValid(username: Editable): Boolean {
        return valueIsNotEmpty(username) && !username.contains(' ')
    }

    /**
     * Email Validation: Checks if the email is valid.
     * @param email The email to be checked.
     * @return True if the email is valid, false otherwise.
     */
    fun isEmailValid(email: Editable): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    /**
     * Password Validation: Checks if the password is valid.
     * @param password The password to be checked.
     * @return True if the password is valid, false otherwise.
     */
    fun isPasswordValid(password: Editable): Boolean {
        return password.length > 5
    }
}