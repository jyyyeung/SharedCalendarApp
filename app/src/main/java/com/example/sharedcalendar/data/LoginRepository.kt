package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiClient
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.RegisterResponse
import com.example.sharedcalendar.models.User

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

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
            // If Login is successful
            sessionManager.setAuthToken(result.data.token)
            setLoggedInUser(sessionManager, result.data.user)
        }
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
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        sessionManager.setUser(loggedInUser)
    }
}