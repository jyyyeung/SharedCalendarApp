package com.example.sharedcalendar.data

import android.content.SharedPreferences
import android.util.Log
import com.example.sharedcalendar.ApiClient
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.RegisterResponse
import com.example.sharedcalendar.models.User
import com.example.sharedcalendar.models.UserSettings
import com.google.gson.Gson
import java.io.IOException

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
        username: String, password: String, sessionManager: SessionManager
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
        username: String, email: String, password: String, sessionManager: SessionManager
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
        sessionManager: SessionManager, sharedPreferences: SharedPreferences, key: String
    ): Result<User> {
        Log.i(TAG, "Will update user preferences")
        val userSettings = sessionManager.getUser()?.settings
        val userId = sessionManager.getUser()?.id
        val updatedSettings = mutableMapOf<String, String>()
        val apiService = ApiClient(sessionManager).apiService
        val updatedUser = mutableMapOf<String, Any>()

        if (key != "name" && userSettings != null && userSettings is UserSettings) {
            // Get current settings if updated pref is not "name"
            updatedSettings["default_view"] = userSettings.defaultView ?: "month"
            updatedSettings["default_calendar"] = userSettings.defaultCalendar ?: "default"
            updatedSettings["default_reminder"] = userSettings.defaultReminder ?: "push"
            updatedSettings["default_timezone"] = userSettings.defaultTimezone ?: "UTC"
        }

        val newValue = sharedPreferences.getString(key, "")
        // Check if new preferences value is empty
        if (newValue.isNullOrEmpty()) {
            return Result.Error(IOException("Value for $key is Null or Empty"))
        }

        Log.i(
            TAG, "now setting $key to ${sharedPreferences.getString(key, null)}"
        )

        // Parse updated preference to required format in DB
        when (key) {
            "name" -> updatedUser["name"] = newValue
            "default_view" -> updatedSettings["default_view"] = newValue
            "default_reminder" -> updatedSettings["default_reminder"] = newValue
            "default_calendar" -> updatedSettings["default_calendar"] = newValue
            "default_timezone" -> updatedSettings["default_timezone"] = newValue
        }

        if (updatedSettings.isNotEmpty()) // Check if Settings have been changed
            updatedUser["settings"] = updatedSettings

        Log.i(TAG, "Calling patchUserById")
        if (userId !is Int) {
            return Result.Error(IOException("UserID not Int, $userId"))
        }
        if (updatedUser.isEmpty()) return Result.Error(IOException("$key is not saved in database, skipping update"))

        val result = dataSource.updateUser(apiService, userId, updatedUser)

        if (result is Result.Success) {
            Log.i(TAG, "Result is Success, updated user already ")
        }
        Log.i(TAG, "Returning Result: $result")
        return result
    }
}