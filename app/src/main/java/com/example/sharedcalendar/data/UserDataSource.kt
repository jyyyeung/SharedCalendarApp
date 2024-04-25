package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiService
import com.example.sharedcalendar.ApiServiceNoAuth
import com.example.sharedcalendar.getDeviceName
import com.example.sharedcalendar.models.LoginRequest
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.RegisterRequest
import com.example.sharedcalendar.models.RegisterResponse
import com.example.sharedcalendar.models.User
import retrofit2.Response
import java.io.IOException


//const val TAG = "LoginDataSource"

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class UserDataSource {
    private val deviceName = getDeviceName()

    suspend fun login(
        email: String, password: String, apiServiceNoAuth: ApiServiceNoAuth
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
        username: String, email: String, password: String, apiServiceNoAuth: ApiServiceNoAuth
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
            val response: Response<Any> = apiService.logout()
            Log.d(TAG, response.toString())
            Log.d(TAG, response.body().toString())

//            return Result.Error(IOException("Function not implemented"))
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
//            return Result.Error(IOException("Error logging in", e))
        }
    }

    companion object {
        private val TAG: String = UserDataSource::class.java.name
    }


    /**
     * Get User By Id.
     *
     * @param apiService
     * @param userId the id of the user to get
     * @return [Result] the User
     */
    suspend fun getUserById(apiService: ApiService, userId: Int): Result<User> {
        return try {
            val response: Response<User> = apiService.getUserById(userId)
            if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                val userResponse: User = response.body()!!
                Result.Success(userResponse)
            } else {
                Result.Error(IOException("Error fetching user by id"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching this user", e))
        }
    }

    suspend fun updateUser(
        apiService: ApiService, userId: Int, updatedUser: Any
//        prefKey: String
    ): Result<User> {
        if (updatedUser !is User && updatedUser !is Map<*, *>) {
            return Result.Error(IOException("Invalid Input Type for updated User"))
        }
        return try {
            val response: Response<User> = apiService.patchUserById(userId, updatedUser)
            Log.i(TAG, "Response for updateUser is $response")

            if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                val userResponse: User = response.body()!!
                Result.Success(userResponse)
            } else {
                Result.Error(IOException("Response not Successful, Error updating user settings"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error Updating user preferences", e))
        }
    }
}