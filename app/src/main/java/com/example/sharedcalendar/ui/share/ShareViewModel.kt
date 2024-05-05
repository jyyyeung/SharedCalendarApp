package com.example.sharedcalendar.ui.share

import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the login screen.
 * @constructor Creates a new instance of [ShareViewModel].
 */
class ShareViewModel : ViewModel() {

    companion object {
        private val TAG: String = ShareViewModel::class.java.name
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

}