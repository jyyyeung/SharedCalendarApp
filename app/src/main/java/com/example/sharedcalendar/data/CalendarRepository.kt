package com.example.sharedcalendar.data

import com.example.sharedcalendar.models.Calendar

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class CalendarRepository(val dataSource: CalendarDataSource) {

    // in-memory cache of the loggedInUser object
    var calendars: List<Calendar?>? = null
        private set


//    val isLoggedIn: Boolean
//        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        calendars = listOf()
    }

//    /**
//     * Logout.
//     *
//     * @param sessionManager
//     */
//    suspend fun logout(sessionManager: SessionManager) {
//        user = null
//        sessionManager.setAuthToken("")
//        val apiService = ApiClient(sessionManager).apiService
//        dataSource.logout(apiService)
//    }
//
//    /**
//     * Login.
//     *
//     * @param username
//     * @param password
//     * @param sessionManager
//     * @return [Result]
//     */
//    suspend fun login(
//        username: String,
//        password: String,
//        sessionManager: SessionManager
//    ): Result<LoginResponse> {
//        val apiServiceNoAuth = ApiClient(sessionManager).apiServiceNoAuth
//
//        // handle login
//        val result = dataSource.login(username, password, apiServiceNoAuth)
//        Log.i(TAG, result.toString())
//        if (result is Result.Success) {
//            // If Login is successful
//            sessionManager.setAuthToken(result.data.token)
//            setLoggedInUser(result.data.user)
//        }
//        return result
//    }
//
//    /**
//     * Login.
//     *
//     * @param username
//     * @param password
//     * @param sessionManager
//     * @return [Result]
//     */
//    suspend fun register(
//        username: String,
//        email: String,
//        password: String,
//        sessionManager: SessionManager
//    ): Result<RegisterResponse> {
//        val apiServiceNoAuth = ApiClient(sessionManager).apiServiceNoAuth
//
//        // handle login
//        val result = dataSource.register(username, email, password, apiServiceNoAuth)
//        Log.i(TAG, result.toString())
//        if (result is Result.Success) {
//            // If Login is successful
//            sessionManager.setAuthToken(result.data.token)
//            setLoggedInUser(result.data.user)
//        }
//        return result
//    }

    /**
     * Set Logged In User.
     *
     * @param calendars
     */
    private fun setCalendars(calendars: List<Calendar>) {
        this.calendars = calendars
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}