package com.example.sharedcalendar.data

import android.content.SharedPreferences
import android.util.Log
import com.example.sharedcalendar.ApiClient
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.RegisterResponse
import com.example.sharedcalendar.models.User
import com.example.sharedcalendar.models.UserSettings
import com.google.gson.Gson

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UserRepository(val dataSource: UserDataSource) {

    companion object {
        private val TAG: String = UserRepository::class.java.name
    }

    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set


    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    /**
     * Logout.
     *
     * @param sessionManager
     */
    suspend fun logout(sessionManager: SessionManager) {
        user = null
        sessionManager.setAuthToken("")
        sessionManager.setUser(null)
        val apiService = ApiClient(sessionManager).apiService
        dataSource.logout(apiService)
    }

    /**
     * Login.
     *
     * @param username
     * @param password
     * @param sessionManager
     * @return [Result]
     */
    suspend fun login(
        username: String,
        password: String,
        sessionManager: SessionManager
    ): Result<LoginResponse> {
        val apiServiceNoAuth = ApiClient(sessionManager).apiServiceNoAuth

        // handle login
        val result = dataSource.login(username, password, apiServiceNoAuth)
        Log.i(TAG, result.toString())

        if (result is Result.Success) {
            Log.i(TAG, "Result is Success, will set auth token and logged in user ")
            // If Login is successful
            sessionManager.setAuthToken(result.data.token)
            val user = result.data.user
            Log.i(TAG, user.settings.toString())
            val gson = Gson()
            if (user.settings is String) {
                user.settings = gson.fromJson(user.settings as String, UserSettings::class.java)
//                Log.i(TAG, user.settings.toString())
            }
            setLoggedInUser(sessionManager, result.data.user)
        }
        Log.i(TAG, "Returning Result: $result")
        return result
    }

    /**
     * Login.
     *
     * @param username
     * @param password
     * @param sessionManager
     * @return [Result]
     */
    suspend fun register(
        username: String,
        email: String,
        password: String,
        sessionManager: SessionManager
    ): Result<RegisterResponse> {
        val apiServiceNoAuth = ApiClient(sessionManager).apiServiceNoAuth

        // handle login
        val result = dataSource.register(username, email, password, apiServiceNoAuth)
        Log.i(TAG, result.toString())
        if (result is Result.Success) {
            // If Login is successful
            sessionManager.setAuthToken(result.data.token)
            setLoggedInUser(sessionManager, result.data.user)
        }
        return result
    }

    /**
     * Set Logged In User.
     *
     * @param loggedInUser
     */
    private fun setLoggedInUser(sessionManager: SessionManager, loggedInUser: User) {
        Log.i(TAG, "Setting Logged in User: $loggedInUser")
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        sessionManager.setUser(loggedInUser)
        Log.i(TAG, "Finish Setting User: $loggedInUser")
    }

    suspend fun updateUserSettings(
        sessionManager: SessionManager,
        sharedPreferences: SharedPreferences,
        key: String
    ) {
        val updatedUser = sessionManager.getUser()
        val apiService = ApiClient(sessionManager).apiService
        if (updatedUser?.settings == null) {
            updatedUser?.settings = UserSettings(
                defaultView = null,
                defaultCalendar = null,
                defaultTimezone = null,
                defaultReminder = "Push"
            )
        }
        if (updatedUser?.settings !is UserSettings) {
            return
        }

        if (key == "calendar_view") {
            (updatedUser.settings as UserSettings).defaultView =
                sharedPreferences.getString(key, null)
        }
        val userId: Int = updatedUser.id

        apiService.patchUserById(userId, updatedUser)

    }
}