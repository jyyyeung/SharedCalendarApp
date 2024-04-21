package com.example.sharedcalendar.data

import android.content.Context
import android.content.SharedPreferences
import com.example.sharedcalendar.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = ""
        const val API_BASE_URL = "http://10.0.2.2:8000/api/"
    }

    /**
     * Save the logged in user token
     */
    fun setAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun setAPIBaseURL(apiBaseUrl: String) {
        val editor = prefs.edit()
        editor.putString(API_BASE_URL, apiBaseUrl)
        editor.apply()
    }

    fun getAPIBaseURL(): String? {
        return prefs.getString(API_BASE_URL, null)
    }
}