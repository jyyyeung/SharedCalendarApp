package com.example.sharedcalendar

import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.LoginRequest
import com.example.sharedcalendar.models.LoginResponse
import com.example.sharedcalendar.models.RegisterRequest
import com.example.sharedcalendar.models.RegisterResponse
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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
    @POST("logout")
    suspend fun logout(): Response<Any>

    /**
     * Returns a list of all calendars the user is allowed to view.
     * @return A list of all calendars.
     */
    @GET("calendars")
    suspend fun getAllCalendars(): Response<List<Calendar>>


    /**
     * Create New Calendar.
     *
     * @return [Response]
     */
    @POST("calendars")
    suspend fun createNewCalendar(): Response<List<Calendar>>


    @GET("calendars/me")
    suspend fun getAllPersonalCalendars(): Response<List<Calendar>>

    @GET("calendars/shared")
    suspend fun getAllSharedCalendars(): Response<List<Calendar>>


    /**
     * Returns a calendar by id.
     * @param id The id of the calendar.
     * @return The calendar with the given id.
     */
    @GET("calendars/{calendar}")
    suspend fun getCalendarById(
        @Path("calendar") calendarId: Int,
    ): Response<Calendar>

    @PATCH("calendars/{calendar}")
    suspend fun patchCalendarById(
        @Path("calendar") calendarId: Int,
    ): Response<Calendar>

    @DELETE("calendars/{calendar}")
    suspend fun deleteCalendarById(
        @Path("calendar") calendarId: Int,
    )

    /**
     * Returns a list of all shares.
     * @return A list of all shares.
     */
    @GET("calendars/{calendar}/events")
    suspend fun getAllCalendarEvents(@Path("calendar") calendarId: Int): Response<List<Event>>

    @POST("calendars/{calendar}/events")
    suspend fun createNewCalendarEvent(@Path("calendar") calendarId: Int): Response<List<Event>>

    @GET("calendars/{calendar}/shares")
    suspend fun getCalendarShares(@Path("calendar") calendarId: Int): Response<List<Share>>

    @POST("calendars/{calendar}/shares")
    suspend fun createCalendarShare(@Path("calendar") calendarId: Int): Response<List<Share>>

    /**
     * Returns an event by id.
     * @param eventId The id of the event.
     * @return The event with the given id.
     */
    @GET("events/{event}")
    suspend fun getEventById(
        @Path("event") eventId: Int,
    ): Response<Event>

    @PATCH("events/{event}")
    suspend fun patchEventById(
        @Path("event") eventId: Int
    ): Response<Event>

    @DELETE("events/{event}")
    suspend fun deleteEventById(
        @Path("event") eventId: Int
    )


    /**
     * Returns a share by id.
     * @param shareId The id of the share.
     * @return The share with the given id.
     */
    @GET("shares/{share}")
    suspend fun getShareById(
        @Path("share") shareId: Int,
    ): Response<Share>

    @PATCH("shares/{share}")
    suspend fun patchShareById(
        @Path("share") shareId: Int,
    ): Response<Share>

    @DELETE("shares/{shareId}")
    suspend fun deleteShareById(
        @Path("shareId") id: Int,
    )

    @GET("user")
    suspend fun getUser()

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/{user}")
    suspend fun getUserById(
        @Path("user") userId: Int
    ): Response<User>

    @PATCH("users/{user}")
    suspend fun patchUserById(
        @Path("user") userId: Int
    ): Response<User>

    @DELETE("users/{user}")
    suspend fun deleteUserById(
        @Path("user") userId: Int
    )
}
