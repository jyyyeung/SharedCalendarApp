package com.example.sharedcalendar.data

import android.util.Log
import com.example.sharedcalendar.ApiService
import com.example.sharedcalendar.models.Share
import retrofit2.Response
import java.io.IOException


class ShareDataSource {
    private val TAG = "ShareDataSource"

    /**
     * Get All Calendar Shares.
     *
     * @param apiService
     * @param calendarId The id of the calendar to get shares for
     * @return Return [Result] the list of all share instances from this calendar
     */
    suspend fun getAllCalendarShares(apiService: ApiService, calendarId: Int): Result<List<Share>> {
        return try {
            val response: Response<List<Share>> = apiService.getCalendarShares(calendarId)
            if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                val shareResponse: List<Share> = response.body()!!
                Result.Success(shareResponse)
            } else {
                Result.Error(IOException("Error fetching all shares of this calendar"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching all calendar shares", e))
        }
    }

    /**
     * Get Share By Id.
     *
     * @param apiService
     * @param shareId the id of the share to get
     * @return [Result] the Share
     */
    suspend fun getShareById(apiService: ApiService, shareId: Int): Result<Share> {
        return try {
            val response: Response<Share> = apiService.getShareById(shareId)
            if (response.isSuccessful) {
                Log.i(TAG, response.body().toString())
                val shareResponse: Share = response.body()!!
                Result.Success(shareResponse)
            } else {
                Result.Error(IOException("Error fetching share by id"))
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.message.toString())
            Result.Error(IOException("Error fetching this share", e))
        }
    }

}