package com.example.sharedcalendar.data

import com.example.sharedcalendar.data.model.LoggedInUser
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.User
import com.example.sharedcalendar.ui.login.LoginActivity

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

    suspend fun logout() {
        user = null
        LoginActivity.sessionManager.setAuthToken("")
        dataSource.logout()
    }

    suspend fun login(
        username: String,
        password: String,
        sessionManager: SessionManager
    ): Result<LoginResponse> {
        // handle login
        val result = dataSource.login(username, password)

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