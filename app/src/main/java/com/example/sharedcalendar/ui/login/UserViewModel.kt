package com.example.sharedcalendar.ui.login

import android.text.Editable
import androidx.core.text.trimmedLength
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel

private val TAG: String = UserViewModel::class.java.name

/**
 * ViewModel for the login screen.
 * @constructor Creates a new instance of [UserViewModel].
 */
class UserViewModel : ViewModel() {

//    fun updateUserSettings(
//        sharedPreferences: SharedPreferences?,
//        key: String?
//    ) {
//        val user = Firebase.auth.currentUser
//        val db = Firebase.firestore
//
//        if (key == "name") {
//            val newName = sharedPreferences?.getString(key, null)
//            if (newName != null) {
//                val profileUpdates = userProfileChangeRequest {
//                    displayName = newName
//                }
//
//                user!!.updateProfile(profileUpdates)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d(TAG, "User profile updated.")
//                        }
//                    }
//
//                val docData = hashMapOf("name" to newName)
//                db.collection("users").document(user.uid).set(docData, SetOptions.merge())
//            }
//
//        }
//
//
//    }

    fun valuesAreEqual(value1: Editable, value2: Editable): Boolean {
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

    // TODO: Add password validation
    fun String.isPasswordValid(): Boolean {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
        return passwordRegex.toRegex().matches(this)
    }

    /**
     * Password Validation: Checks if the password is valid.
     * @param password The password to be checked.
     * @return True if the password is valid, false otherwise.
     */
    fun isPasswordValid(password: Editable): Boolean {
        return password.toString().isNotBlank() && password.toString().trimmedLength() > 5
    }
}
