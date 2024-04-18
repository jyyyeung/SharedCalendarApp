package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiClient
import com.example.sharedcalendar.models.LoginResponse
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

    suspend fun logout(sessionManager: SessionManager) {
        user = null
        sessionManager.setAuthToken("")
        val apiService = ApiClient(sessionManager).apiService
        dataSource.logout(apiService)
    }

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
            setLoggedInUser(result.data.user)
        }
        return result
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}