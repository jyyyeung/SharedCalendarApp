package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.data.model.LoggedInUser
import java.io.IOException

const val TAG = "LoginDataSource"
/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            Log.i(TAG, username)
            Log.i(TAG, password)
            // TODO: handle loggedInUser authentication

            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}