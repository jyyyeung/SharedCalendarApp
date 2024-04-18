package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.data.Result
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.ui.login.LoginActivity
import retrofit2.Response
import java.io.IOException

class CalendarDataSource {
    private val apiService = LoginActivity.apiService
    private val TAG = "CalendarDataSource"
    suspend fun getAllCalendars(): Result<List<Calendar>> {
        return try {
            val response: Response<List<Calendar>> = apiService.getAllCalendars()
            if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                val calendarResponse: List<Calendar> = response.body()!!
                Result.Success(calendarResponse)
            } else {
                Result.Error(IOException("Error fetching all calendars"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching all calendars", e))
        }
    }
}