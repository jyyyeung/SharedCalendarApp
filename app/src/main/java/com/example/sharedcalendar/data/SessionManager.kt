package com.example.sharedcalendar.data

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.sharedcalendar.R
import com.example.sharedcalendar.models.User

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private var editor: Editor = prefs.edit()

    companion object {
        const val USER_TOKEN = "userToken"
        const val API_BASE_URL = "apiBaseUrl"
        const val USER_USERNAME = "userUsername"
        const val USER_NAME = "userName"
        const val USER_EMAIL = "userEmail"
        const val USER_ID = "userId"
    }

    /**
     * Save the logged in user token
     */
    fun setAuthToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun setAPIBaseURL(apiBaseUrl: String) {
        editor.putString(API_BASE_URL, apiBaseUrl)
        editor.apply()
    }

    fun getAPIBaseURL(): String? {
        return prefs.getString(API_BASE_URL, null)
    }

    fun setUser(user: User? = null) {
        if (user == null) {
            editor.remove(USER_USERNAME)
            editor.remove(USER_EMAIL)
            editor.remove(USER_ID)
            editor.remove(USER_USERNAME)

        } else {
            editor.putString(USER_USERNAME, user.username)
            editor.putString(USER_EMAIL, user.email)
            editor.putInt(USER_ID, user.id)
            editor.putString(USER_NAME, user.name)
        }
        editor.apply()
    }

    fun getUser(): User? {
        if (getAuthToken() == null)
            return null

        return User(
            id = prefs.getInt(USER_ID, -1),
            username = prefs.getString(USER_USERNAME, "")!!,
            name = prefs.getString(USER_NAME, "")!!,
            email = prefs.getString(USER_EMAIL, "")!!
        )
    }
}