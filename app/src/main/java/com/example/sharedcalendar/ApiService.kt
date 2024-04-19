package com.example.sharedcalendar

import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.LoginRequest
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.RegisterRequest
import com.example.sharedcalendar.models.RegisterResponse
import com.example.sharedcalendar.models.Share
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// IMPLEMENTATION EXAMPLE in onCreate
// super.onCreate(savedInstanceState)
// setContentView(R.layout.activity_main)
//
// lifecycleScope.launch {
//    val response = ApiClient.apiService.getAllCalendars()
//    Log.d(TAG, response.toString())
//    if (response.isSuccessful) {
//        launch(Dispatchers.Main) {
//            Log.i(TAG, response.body().toString())
//        }
//    }
//
//    val response2 = ApiClient.apiService.getCalendarById(1)
//    Log.i(TAG, response2.body().toString())
// }

/**
 * Interface for API calls that do not require authentication.
 */
interface ApiServiceNoAuth {
    /**
     * Login with email and password
     * @param login The login request.
     * @return The login response.
     */
    @POST("login")
    suspend fun login(
        @Body login: LoginRequest,
    ): Response<LoginResponse>

    /**
     * Register for a new account
     * @param register The register request.
     * @return The register response.
     */
    @POST("register")
    suspend fun register(
        @Body register: RegisterRequest,
    ): Response<RegisterResponse>
}

interface ApiService {
    /**
     * Logout user
     */
    suspend fun logout(): Response<Any>

    /**
     * Returns a list of all calendars the user is allowed to view.
     * @return A list of all calendars.
     */
    @GET("calendars")
    suspend fun getAllCalendars(): Response<List<Calendar>>

    /**
     * Returns a calendar by id.
     * @param id The id of the calendar.
     * @return The calendar with the given id.
     */
    @GET("calendars/{id}")
    suspend fun getCalendarById(
        @Path("id") id: Int,
    ): Response<Calendar>

    /**
     * Returns a list of all shares.
     * @return A list of all shares.
     */
    @GET("events")
    suspend fun getAllEvents(): Response<List<Event>>

    /**
     * Returns an event by id.
     * @param id The id of the event.
     * @return The event with the given id.
     */
    @GET("events/{id}")
    suspend fun getEventById(
        @Path("id") id: Int,
    ): Response<Event>

    /**
     * Returns a list of all shares.
     * @return A list of all shares.
     */
    @GET("shares")
    suspend fun getAllShares(): Response<List<Share>>

    /**
     * Returns a share by id.
     * @param id The id of the share.
     * @return The share with the given id.
     */
    @GET("shares/{id}")
    suspend fun getShareById(
        @Path("id") id: Int,
    ): Response<Share>
}
