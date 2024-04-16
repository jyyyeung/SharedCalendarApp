package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiClient
import com.example.sharedcalendar.getDeviceName
import com.example.sharedcalendar.models.LoginRequest
import com.example.sharedcalendar.models.LoginResponse
import retrofit2.Response
import java.io.IOException


const val TAG = "LoginDataSource"

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {


    suspend fun login(username: String, password: String): Result<LoginResponse> {
        try {
            Log.i(TAG, username)
            Log.i(TAG, password)
            val deviceName = getDeviceName()
            //  handle loggedInUser authentication
            val credentials = LoginRequest(username, password, deviceName)
            Log.i(TAG, credentials.toString())
            val response: Response<LoginResponse> = ApiClient.apiService.login(credentials)
            Log.d(TAG, response.toString())
            Log.d(TAG, response.body().toString())
            return if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                // TODO: use real login credentials and save token

                // TODO: `LoginResponse(token=null, status=404, message=Not Found!!!)` still logins user
//                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
                val loginResponse: LoginResponse = response.body()!!
                Result.Success(loginResponse)
            } else {
                Result.Error(IOException("Error logging in"))
            }
//            return Result.Error(IOException("Function not implemented"))
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}