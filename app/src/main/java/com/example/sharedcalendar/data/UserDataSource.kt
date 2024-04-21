package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiService
import com.example.sharedcalendar.models.User
import retrofit2.Response
import java.io.IOException


class UserDataSource {
    private val TAG = "UserDataSource"


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

}