package com.example.sharedcalendar

import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.GetAllCalendarsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("calendars")
    suspend fun getAllCalendars(): Response<GetAllCalendarsResponse>

    @GET("calendars/{id}")
    suspend fun getCalendarById(@Path("id") id: Int): Response<Calendar>

    
}