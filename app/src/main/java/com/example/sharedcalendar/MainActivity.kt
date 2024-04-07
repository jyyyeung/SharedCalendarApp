package com.example.sharedcalendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sharedcalendar.ApiClient.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val response = apiService.getAllCalendars()
            Log.d(TAG, response.toString())
            if (response.isSuccessful) {
                launch(Dispatchers.Main) {
                    Log.i(TAG, response.body()?.calendars.toString())
                }
            }

            val response2 = apiService.getCalendarById(1)
            Log.i(TAG, response2.body().toString())
        }
    }

}