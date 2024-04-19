package com.example.sharedcalendar.models

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    var email: String, var password: String, @SerializedName("device_name") var deviceName: String
)


data class LoginResponse(
    var token: String,
    var user: User,
    @SerializedName("token_type") var tokenType: String?,
    var status: String?,
    var message: String?
)
