package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiService
import com.example.sharedcalendar.models.Calendar
import retrofit2.Response
import java.io.IOException


class CalendarDataSource {
    private val TAG = "CalendarDataSource"

    /**
     * Get All Calendars.
     *
     * @param apiService
     * @return Return [Result]
     */
    suspend fun getAllCalendars(apiService: ApiService): Result<List<Calendar>> {
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

//    suspend fun createNewCalendar(apiService: ApiService): Result<Calendar>{
//        return try{
//
//        }
//    }

    suspend fun getAllPersonalCalendars(apiService: ApiService): Result<List<Calendar>> {
        return try {
            val response: Response<List<Calendar>> = apiService.getAllPersonalCalendars()
            if (response.isSuccessful) {
                val calendarResponse: List<Calendar> = response.body()!!
                Result.Success(calendarResponse)
            } else {
                Result.Error(IOException("Error fetching all personal calendars"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching all personal calendars", e))
        }
    }

    suspend fun getAllSharedCalendars(apiService: ApiService): Result<List<Calendar>> {
        return try {
            val response: Response<List<Calendar>> = apiService.getAllSharedCalendars()
            if (response.isSuccessful) {
                val calendarResponse: List<Calendar> = response.body()!!
                Result.Success(calendarResponse)
            } else {
                Result.Error(IOException("Error fetching all shared calendars"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching all shared calendars", e))
        }
    }
}