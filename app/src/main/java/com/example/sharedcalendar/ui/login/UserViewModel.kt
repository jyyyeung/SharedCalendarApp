package com.example.sharedcalendar.ui.login

import android.content.SharedPreferences
import android.text.Editable
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.data.SessionManager
import com.example.sharedcalendar.data.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

private val TAG: String = UserViewModel::class.java.name

/**
 * ViewModel for the login screen.
 * @property userRepository The login repository to be used.
 * @constructor Creates a new instance of [UserViewModel].
 * @see UserRepository
 */
class UserViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun updateUserSettings(
        sessionManager: SessionManager,
        sharedPreferences: SharedPreferences?,
        key: String?
    ) {
        val user = Firebase.auth.currentUser

        if (key == "name") {
            val profileUpdates = userProfileChangeRequest {
                displayName = sharedPreferences?.getString(key, null)
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }
        }


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
