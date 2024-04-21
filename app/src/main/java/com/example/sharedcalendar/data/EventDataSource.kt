package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiService
import com.example.sharedcalendar.models.Event
import retrofit2.Response
import java.io.IOException


class EventDataSource {
    private val TAG = "EventDataSource"

    /**
     * Get All Calendars.
     *
     * @param apiService
     * @param calendarId The id of the calendar to get events for
     * @return Return [Result] the list of all events from this calendar
     */
    suspend fun getAllCalendarEvents(apiService: ApiService, calendarId: Int): Result<List<Event>> {
        return try {
            val response: Response<List<Event>> = apiService.getAllCalendarEvents(calendarId)
            if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                val eventResponse: List<Event> = response.body()!!
                Result.Success(eventResponse)
            } else {
                Result.Error(IOException("Error fetching all calendars"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching all calendars", e))
        }
    }

    suspend fun getEventById(apiService: ApiService, eventId: Int): Result<Event> {
        return try {
            val response: Response<Event> = apiService.getEventById(eventId)
            if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                val eventResponse: Event = response.body()!!
                Result.Success(eventResponse)
            } else {
                Result.Error(IOException("Error fetching event"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching event", e))
        }
    }

}