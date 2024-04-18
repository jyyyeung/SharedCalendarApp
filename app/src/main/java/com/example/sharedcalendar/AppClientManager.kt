package com.example.sharedcalendar

import com.example.sharedcalendar.data.SessionManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val DEFAULT_BASE_URL: String = "http://10.0.2.2:8000/api/"

class ApiClient(sessionManager: SessionManager) {
    // Need to enable wifi in emulator to work
    private val baseUrl: String = sessionManager.getAPIBaseURL() ?: DEFAULT_BASE_URL

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private fun requestBuilder(chain: Interceptor.Chain): Request.Builder {
        return chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Au", "application/json")
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor { chain ->
            chain.proceed(
                requestBuilder(chain).addHeader(
                    "Authorization",
                    "Bearer " + sessionManager.getAuthToken()
                ).build()
            )
        }.build()
    }

    private val httpClientNoAuth: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor { chain ->
            chain.proceed(requestBuilder(chain).build())
        }.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val retrofitNoAuth: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClientNoAuth)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val apiServiceNoAuth: ApiServiceNoAuth by lazy {
        retrofit.create(ApiServiceNoAuth::class.java)
    }
}
