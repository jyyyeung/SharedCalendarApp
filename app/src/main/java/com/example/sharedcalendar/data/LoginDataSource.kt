package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiService
import com.example.sharedcalendar.ApiServiceNoAuth
import com.example.sharedcalendar.getDeviceName
import com.example.sharedcalendar.models.LoginRequest
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.RegisterRequest
import com.example.sharedcalendar.models.RegisterResponse
import retrofit2.Response
import java.io.IOException


const val TAG = "LoginDataSource"

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val deviceName = getDeviceName()

    suspend fun login(
        email: String,
        password: String,
        apiServiceNoAuth: ApiServiceNoAuth
    ): Result<LoginResponse> {
        try {
            //  handle loggedInUser authentication
            val credentials = LoginRequest(email, password, deviceName)
//            Log.i(TAG, credentials.toString())
            val response: Response<LoginResponse> = apiServiceNoAuth.login(credentials)
//            Log.d(TAG, response.toString())
//            Log.d(TAG, response.body().toString())
            return if (response.isSuccessful) {
//                Log.i(TAG, response.body().toString())
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

    suspend fun register(
        username: String,
        email: String,
        password: String,
        apiServiceNoAuth: ApiServiceNoAuth
    ): Result<RegisterResponse> {
        try {
            //  handle loggedInUser authentication
            val credentials = RegisterRequest(username, email, password, deviceName)
            Log.i(TAG, credentials.toString())
            val response: Response<RegisterResponse> = apiServiceNoAuth.register(credentials)
            Log.d(TAG, response.toString())
//            Log.d(TAG, response.body().toString())
            return if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                // TODO: use real login credentials and save token

                // TODO: `LoginResponse(token=null, status=404, message=Not Found!!!)` still logins user
//                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
                val registerResponse: RegisterResponse = response.body()!!
                Result.Success(registerResponse)
            } else {
                Result.Error(IOException("Error Registering"))
            }
//            return Result.Error(IOException("Function not implemented"))
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            return Result.Error(IOException("Error Registering", e))
        }
    }

    suspend fun logout(apiService: ApiService) {
        // TODO: revoke authentication
        try {
            val response: Response<Any> =
                apiService.logout()
            Log.d(TAG, response.toString())
            Log.d(TAG, response.body().toString())

//            return Result.Error(IOException("Function not implemented"))
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
//            return Result.Error(IOException("Error logging in", e))
        }
    }
}