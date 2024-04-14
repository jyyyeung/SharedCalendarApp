package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiClient
import com.example.sharedcalendar.data.model.LoggedInUser
import com.example.sharedcalendar.models.LoginRequest
import java.io.IOException

const val TAG = "LoginDataSource"

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            Log.i(TAG, username)
            Log.i(TAG, password)

            //  handle loggedInUser authentication
            val credentials = LoginRequest(username, password)
            val response = ApiClient.apiService.login(credentials)
            Log.d(TAG, response.toString())
            return if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                // TODO: use real login credentials and save token
                // TODO: `LoginResponse(token=null, status=404, message=Not Found!!!)` still logins user
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
                Result.Success(fakeUser)
            } else {
                Result.Error(IOException("Error logging in"))

            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}