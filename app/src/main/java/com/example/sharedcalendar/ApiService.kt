package com.example.sharedcalendar

import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("calendars")
    suspend fun getAllCalendars(): Response<List<Calendar>>

    @GET("calendars/{id}")
    suspend fun getCalendarById(@Path("id") id: Int): Response<Calendar>

    @GET("events")
    suspend fun getAllEvents(): Response<List<Event>>

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: Int): Response<Event>
}